//package com.simple.grpc.application.controller;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/demo")
//public class DemoController {
//
//    @Autowired
//    private UserServiceGrpc.UserServiceBlockingStub userServiceGrpc;
//
//    @GetMapping("/get")
//    public String get(@RequestParam("id") Integer id) {
//        // 创建请求
//        UserGetRequest request = UserGetRequest.newBuilder().setId(id).build();
//        // 执行 gRPC 请求
//        UserGetResponse response = userServiceGrpc.get(request);
//        // 响应
//        return response.getName();
//    }
//
//    @GetMapping("/create") // 为了方便测试，实际使用 @PostMapping
//    public Integer create(@RequestParam("name") String name,
//                          @RequestParam("gender") Integer gender) {
//        // 创建请求
//        UserCreateRequest request = UserCreateRequest.newBuilder()
//                .setName(name).setGender(gender).build();
//        // 执行 gRPC 请求
//        UserCreateResponse response = userServiceGrpc.create(request);
//        // 响应
//        return response.getId();
//    }
//
//}