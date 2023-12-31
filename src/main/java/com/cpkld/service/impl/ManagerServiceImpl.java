package com.cpkld.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cpkld.dto.ManagerDTO;
import com.cpkld.dto.UserDTO;
import com.cpkld.model.entity.Manager;
import com.cpkld.model.exception.existed.ManagerExistedException;
import com.cpkld.model.exception.notfound.ManagerNotFoundException;
import com.cpkld.model.response.ApiResponse;
import com.cpkld.repository.ManagerRepository;
import com.cpkld.service.ManagerService;
import com.cpkld.service.auth.AuthService;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerRepository repo;
    @Autowired
    private AuthService userService;

    private ManagerDTO convertEntityToDto(Manager manager) {
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setManagerId(manager.getId());
        managerDTO.setFullname(manager.getFullName());
        managerDTO.setEmail(manager.getEmail());
        managerDTO.setPhone(manager.getPhoneNumber());
        managerDTO.setPassword(manager.getUser().getPassword());
        managerDTO.setAddress(manager.getAddress());
        return managerDTO;
    }

    @Override
    public ResponseEntity<?> getAll() {
        List<ManagerDTO> managerList = repo.findAll()
            .stream()
            .map(this::convertEntityToDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Success", managerList),
            HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        Optional<Manager> optional = repo.findById(id);
        if (!optional.isPresent()) {
            throw new ManagerNotFoundException("Manager not found");
        }
        return new ResponseEntity<>(
            new ApiResponse<>(
                HttpStatus.OK.value(), 
                "Success", 
                optional.stream().map(this::convertEntityToDto).toList()
            ),
            HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<?> getPaginated(int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").ascending());
        Page<Manager> pageList = repo.findAll(pageable);
        return new ResponseEntity<>(
            new ApiResponse<>(
                HttpStatus.OK.value(), 
                "Success", 
                pageList.stream().map(this::convertEntityToDto).toList()
            ),
            HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<?> add(UserDTO userDTO) {
        Optional<Manager> optional = repo.findByEmail(userDTO.getEmail());
        if (optional.isPresent()) {
            throw new ManagerExistedException("Manager existed!");
        }
        userService.saveManagerAccount(userDTO);
        return new ResponseEntity<>(
            new ApiResponse<>(
                HttpStatus.OK.value(), 
                "Create manager successfully", 
                null
            ),
            HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<?> update(Integer id, ManagerDTO newManagerDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
