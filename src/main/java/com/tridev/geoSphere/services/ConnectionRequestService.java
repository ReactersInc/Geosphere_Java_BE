package com.tridev.geoSphere.services;

import com.tridev.geoSphere.constant.CommonValidationConstant;
import com.tridev.geoSphere.dto.common.PaginatedResponse;
import com.tridev.geoSphere.dto.connectionRequest.ConnectionRequestDTO;
import com.tridev.geoSphere.dto.connectionRequest.ConnectionResponseDTO;
import com.tridev.geoSphere.entities.sql.ConnectionRequestEntity;
import com.tridev.geoSphere.entities.sql.UserContactsEntity;
import com.tridev.geoSphere.enums.Status;
import com.tridev.geoSphere.exceptions.BadRequestException;
import com.tridev.geoSphere.exceptions.ResourceNotFoundException;
import com.tridev.geoSphere.mappers.ConnectionRequestMapper;
import com.tridev.geoSphere.repositories.sql.ConnectionRequestRepository;
import com.tridev.geoSphere.repositories.sql.UserContactsRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConnectionRequestService {


    @Autowired
    private JwtUtil jwtUtil;



     private final ConnectionRequestRepository connectionRequestRepository;
     private final ConnectionRequestMapper connectionRequestMapper;

     @Autowired
     private UserContactsRepository userContactsRepository;



     public ConnectionRequestService(ConnectionRequestRepository connectionRequestRepository, ConnectionRequestMapper connectionRequestMapper) {
         this.connectionRequestRepository = connectionRequestRepository;
         this.connectionRequestMapper = connectionRequestMapper;
     }

//     public List<ConnectionResponeDTO> getConnectionRequestsByRequesterUserIdAndStatus(Long requesterUserId, Integer status) {
//         return connectionRequestRepository.findByRequesterUserIdAndStatus(requesterUserId, status);
//     }


    @Transactional
    public BaseResponse createConnectionRequest(ConnectionRequestDTO connectionRequestDTO) throws  Exception{
        Long requesterUserId = jwtUtil.getUserIdFromToken();
        log.info("the current userId is {}",requesterUserId);
        Optional<ConnectionRequestEntity> byRequesterUserIdAndTargetUserIdAndStatus = connectionRequestRepository.findByRequesterUserIdAndTargetUserIdAndStatus(requesterUserId, connectionRequestDTO.getTargetUserId(), Status.ACTIVE.getValue());
        if(byRequesterUserIdAndTargetUserIdAndStatus.isPresent()){
            throw new BadRequestException(CommonValidationConstant.CONNECTION_REQUEST_ALREADY_EXISTS);
        }

        ConnectionRequestEntity connectionRequestEntity = connectionRequestMapper.toEntity(connectionRequestDTO);

        connectionRequestEntity.setRequesterUserId(requesterUserId);

        connectionRequestEntity.setStatus(Status.ACTIVE.getValue());

        connectionRequestRepository.save(connectionRequestEntity);

        return GeosphereServiceUtility.getBaseResponseWithoutData();


    }

    public BaseResponse getAllConnectionRequestByTargetId(int page, int size) throws Exception {
        try {
            Long targetUserId = jwtUtil.getUserIdFromToken();
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<ConnectionRequestEntity> connectionPage = connectionRequestRepository.findByTargetUserIdAndStatus(
                    targetUserId,
                    Status.ACTIVE.getValue(),
                    pageable);

            if (connectionPage.isEmpty()) {
                throw new ResourceNotFoundException(CommonValidationConstant.CONNECTION_REQUEST_NOT_FOUND);
            }

            List<ConnectionResponseDTO> responseList = connectionPage.getContent().stream()
                    .map(connectionRequestMapper::toResponseDTO)
                    .collect(Collectors.toList());

            PaginatedResponse<ConnectionResponseDTO> paginatedResponse = new PaginatedResponse<>();
            paginatedResponse.setList(responseList);
            paginatedResponse.setPage(connectionPage.getNumber());
            paginatedResponse.setSize(connectionPage.getSize());
            paginatedResponse.setTotalElements(connectionPage.getTotalElements());
            paginatedResponse.setTotalPages(connectionPage.getTotalPages());

            return GeosphereServiceUtility.getBaseResponse(paginatedResponse);

        } catch (ResourceNotFoundException ex) {
            log.warn("Connection requests not found: {}", ex.getMessage());
            throw ex;
        }
    }


    @Transactional
    public BaseResponse acceptConnectionRequest(Long connectionRequestId) throws Exception{
        Optional<ConnectionRequestEntity> connectionExists = connectionRequestRepository.findByIdAndStatus(connectionRequestId, Status.ACTIVE.getValue());
        if(connectionExists.isPresent()){

            ConnectionRequestEntity entity =  connectionExists.get();



            entity.setStatus(Status.ACCEPTED.getValue());
            connectionRequestRepository.save(entity);

            // Add to user contacts

            UserContactsEntity userContactsEntity = new UserContactsEntity();

            userContactsEntity.setUserId(entity.getRequesterUserId());
            userContactsEntity.setContactUserId(entity.getTargetUserId());

            userContactsEntity.setStatus(Status.ACTIVE.getValue());

            userContactsRepository.save(userContactsEntity);


            return GeosphereServiceUtility.getBaseResponseWithoutData();
        }else{
            throw new BadRequestException(CommonValidationConstant.CONNECTION_REQUEST_NOT_FOUND);
        }
    }


    @Transactional
    public BaseResponse rejectConnectionRequest(Long connectionRequestId) throws Exception{
        Optional<ConnectionRequestEntity> connectionExists = connectionRequestRepository.findByIdAndStatus(connectionRequestId, Status.ACTIVE.getValue());
        if(connectionExists.isPresent()){
            ConnectionRequestEntity entity =  connectionExists.get();

            entity.setStatus(Status.REJECTED.getValue());
            connectionRequestRepository.save(entity);
            return GeosphereServiceUtility.getBaseResponseWithoutData();
        }else{
            throw new BadRequestException(CommonValidationConstant.CONNECTION_REQUEST_NOT_FOUND);
        }
    }


    @Transactional
    public BaseResponse redoConnectionRequest(Long connectionRequestId) throws Exception{
        Optional<ConnectionRequestEntity> connectionExists = connectionRequestRepository.findByIdAndStatus(connectionRequestId, Status.ACTIVE.getValue());
        if(connectionExists.isPresent()){
            ConnectionRequestEntity entity =  connectionExists.get();


            entity.setStatus(Status.CANCELED.getValue());
            connectionRequestRepository.save(entity);
            return GeosphereServiceUtility.getBaseResponseWithoutData();
        }else{
            throw new BadRequestException(CommonValidationConstant.CONNECTION_REQUEST_NOT_FOUND);
        }
    }





}
