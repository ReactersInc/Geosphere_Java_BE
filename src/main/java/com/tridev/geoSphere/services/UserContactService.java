package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.common.PaginatedResponse;
import com.tridev.geoSphere.dto.userContacts.UserContactsResponseDTO;
import com.tridev.geoSphere.entities.sql.UserContactsEntity;
import com.tridev.geoSphere.entities.sql.UserEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.exceptions.ResourceNotFoundException;
import com.tridev.geoSphere.mappers.UserContactsMapper;
import com.tridev.geoSphere.repositories.sql.UserContactsRepository;
import com.tridev.geoSphere.repositories.sql.UserRepo;
import com.tridev.geoSphere.response.BaseResponse;
import com.tridev.geoSphere.utils.GeosphereServiceUtility;
import com.tridev.geoSphere.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserContactService {

    @Autowired
    private UserContactsRepository  userContactsRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserContactsMapper userContactsMapper;

    public BaseResponse getUserContacts(int page, int size) throws Exception {
        try {
            Long userId = jwtUtil.getUserIdFromToken();

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<UserContactsEntity> contactsPage = userContactsRepository.findByUserIdAndStatus(
                    userId,
                    Status.ACTIVE.getValue(),
                    pageable
            );

            if (contactsPage.isEmpty()) {
                throw new BadRequestException(CommonValidationConstant.NO_CONTACTS_FOUND);
            }

            // Step 1: Get all contactUserIds
            List<Long> contactUserIds = contactsPage.getContent().stream()
                    .map(UserContactsEntity::getContactUserId)
                    .distinct()
                    .collect(Collectors.toList());

            // Step 2: Fetch all UserEntities in a single query
            List<UserEntity> userEntities = userRepo.findAllById(contactUserIds);

            // Step 3: Map contactUserId → UserEntity for fast lookup
            Map<Long, UserEntity> userMap = userEntities.stream()
                    .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

            // Step 4: Map to response DTOs using mapper
            List<UserContactsResponseDTO> responseList = contactsPage.getContent().stream()
                    .map(contact -> {
                        UserEntity user = userMap.get(contact.getContactUserId());
                        return user != null ? userContactsMapper.toResponseDTO(user) : null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Step 5: Wrap in paginated response
            PaginatedResponse<UserContactsResponseDTO> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setList(responseList);
            paginatedResponse.setPage(contactsPage.getNumber());
            paginatedResponse.setSize(contactsPage.getSize());
            paginatedResponse.setTotalElements(contactsPage.getTotalElements());
            paginatedResponse.setTotalPages(contactsPage.getTotalPages());

            return GeosphereServiceUtility.getBaseResponse(paginatedResponse);

        } catch (ResourceNotFoundException ex) {
            log.warn("User contacts not found: {}", ex.getMessage());
            throw ex;
        }
    }

}
