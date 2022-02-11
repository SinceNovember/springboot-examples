//package com.simple.grpc.userservice.rpc;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {
//
//    @Override
//    public void get(UserGetRequest request, StreamObserver<UserGetResponse> responseObserver) {
//        // 创建响应对象
//        UserGetResponse.Builder builder = UserGetResponse.newBuilder();
//        builder.setId(request.getId())
//                .setName("没有昵称：" + request.getId())
//                .setGender(request.getId() % 2 + 1);
//        // 返回响应
//        responseObserver.onNext(builder.build());
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void create(UserCreateRequest request, StreamObserver<UserCreateResponse> responseObserver) {
//        // 创建响应对象
//        UserCreateResponse.Builder builder = UserCreateResponse.newBuilder();
//        builder.setId((int) (System.currentTimeMillis() / 1000));
//        // 返回响应
//        responseObserver.onNext(builder.build());
//        responseObserver.onCompleted();
//    }
//
//}