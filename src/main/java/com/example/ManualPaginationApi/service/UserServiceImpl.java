package com.example.ManualPaginationApi.service;

import com.example.ManualPaginationApi.dto.UserDto;
import com.example.ManualPaginationApi.dto.UserResponse;
import com.example.ManualPaginationApi.entity.Users;
import com.example.ManualPaginationApi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Optional<Users> addUser(Users users) {
        if(users != null){
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            return Optional.of(userRepository.save(users));
        }
        return null;
    }

    @Override
    public Optional<Users> updateUser(int id, Users users) {
        if(users != null){
            Users users1 = userRepository.getById(id);
            if (users1 != null){
                users1.setEmail(users.getEmail());
                users1.setFullname(users.getFullname());
                users1.setPhone(users.getPhone());
                users1.setPassword(passwordEncoder.encode(users.getPassword()));
                users1.setRole(users.getRole());

                return Optional.of(userRepository.save(users1));
            }
        }
        return null;
    }

    @Override
    public boolean deleteUser(int id) {
        if (id >= 1){
            Users users = userRepository.getById(id);
            if(users != null){
                userRepository.delete(users);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> getOneUser(int id) {
        return Optional.of(userRepository.findById(id).get());
    }

    @Override
    public UserResponse findPaginated(String email, String phone, String fullname, int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
//        List<Users> listUser = new ArrayList<>();
//        if(email != null && phone != null && fullname != null){
        List<Users>   listUser = userRepository.findByEmailAndPhoneAndFullname(email, phone, fullname);
//        }else {
//            listUser = userRepository.findAll(sort);
//        }

//        List<Users> listUser = userRepository.findAll(sort);
        List<Users> listUserNew = new ArrayList<>();
        UserResponse userResponses = new UserResponse();

        int totalItems = listUser.toArray().length;

        int fromElement = ((pageNo - 1) * pageSize);
        int toElement = pageNo * pageSize;
        if (toElement > totalItems) {
            toElement = totalItems;
        }

        //lấy danh sách user theo trang hiện tại
        for (int i = fromElement; i < toElement; i++) {
            System.out.println("_______i = : " + i);

            listUserNew.add(listUser.get(i));
            System.out.println("user thứ : " + listUser.get(i));
        }

        int totalPages = 0;

        if (totalItems % pageSize != 0) {
            // Trường hợp ...
            totalPages = (totalItems / pageSize) + 1;
        } else {
            // Trường hợp
            totalPages = (totalItems / pageSize);
        }

        List<UserDto> listUserDto = new ArrayList<>();

        // Thực hiện xử lý  ....
        for (int i = 0; i < listUserNew.size(); i++) {
            listUserDto.add(modelMapper.map(listUserNew.get(i), UserDto.class));
        }
        System.out.println("listUsers: " + listUserNew);
        System.out.println("userResponses: " + userResponses);

//        for (Users userElement : listUsers) {
//            listUserDto.add(modelMapper.map(userElement, UserDto.class));
//        }
        userResponses.setData(listUserDto);
        userResponses.setTotalItems(totalItems);
        userResponses.setTotalPages(totalPages);
        userResponses.setCurrentPage(pageNo);

        return userResponses;
    }


}
