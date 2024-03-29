package ru.tinkoff.hse.lib;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.0)",
    comments = "Source: converter.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class CurrencyConverterGrpc {

  private CurrencyConverterGrpc() {}

  public static final java.lang.String SERVICE_NAME = "converter.CurrencyConverter";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ConvertRequest,
          ConvertResponse> getConvertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Convert",
      requestType = ConvertRequest.class,
      responseType = ConvertResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ConvertRequest,
          ConvertResponse> getConvertMethod() {
    io.grpc.MethodDescriptor<ConvertRequest, ConvertResponse> getConvertMethod;
    if ((getConvertMethod = CurrencyConverterGrpc.getConvertMethod) == null) {
      synchronized (CurrencyConverterGrpc.class) {
        if ((getConvertMethod = CurrencyConverterGrpc.getConvertMethod) == null) {
          CurrencyConverterGrpc.getConvertMethod = getConvertMethod =
              io.grpc.MethodDescriptor.<ConvertRequest, ConvertResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Convert"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ConvertRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ConvertResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CurrencyConverterMethodDescriptorSupplier("Convert"))
              .build();
        }
      }
    }
    return getConvertMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CurrencyConverterStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CurrencyConverterStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CurrencyConverterStub>() {
        @java.lang.Override
        public CurrencyConverterStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CurrencyConverterStub(channel, callOptions);
        }
      };
    return CurrencyConverterStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CurrencyConverterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CurrencyConverterBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CurrencyConverterBlockingStub>() {
        @java.lang.Override
        public CurrencyConverterBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CurrencyConverterBlockingStub(channel, callOptions);
        }
      };
    return CurrencyConverterBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CurrencyConverterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CurrencyConverterFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CurrencyConverterFutureStub>() {
        @java.lang.Override
        public CurrencyConverterFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CurrencyConverterFutureStub(channel, callOptions);
        }
      };
    return CurrencyConverterFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void convert(ConvertRequest request,
                         io.grpc.stub.StreamObserver<ConvertResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConvertMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CurrencyConverter.
   */
  public static abstract class CurrencyConverterImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CurrencyConverterGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CurrencyConverter.
   */
  public static final class CurrencyConverterStub
      extends io.grpc.stub.AbstractAsyncStub<CurrencyConverterStub> {
    private CurrencyConverterStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyConverterStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CurrencyConverterStub(channel, callOptions);
    }

    /**
     */
    public void convert(ConvertRequest request,
                        io.grpc.stub.StreamObserver<ConvertResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConvertMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CurrencyConverter.
   */
  public static final class CurrencyConverterBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CurrencyConverterBlockingStub> {
    private CurrencyConverterBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyConverterBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CurrencyConverterBlockingStub(channel, callOptions);
    }

    /**
     */
    public ConvertResponse convert(ConvertRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConvertMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CurrencyConverter.
   */
  public static final class CurrencyConverterFutureStub
      extends io.grpc.stub.AbstractFutureStub<CurrencyConverterFutureStub> {
    private CurrencyConverterFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyConverterFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CurrencyConverterFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ConvertResponse> convert(
        ConvertRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConvertMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CONVERT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONVERT:
          serviceImpl.convert((ConvertRequest) request,
              (io.grpc.stub.StreamObserver<ConvertResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getConvertMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
                    ConvertRequest,
                    ConvertResponse>(
                service, METHODID_CONVERT)))
        .build();
  }

  private static abstract class CurrencyConverterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CurrencyConverterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Converter.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CurrencyConverter");
    }
  }

  private static final class CurrencyConverterFileDescriptorSupplier
      extends CurrencyConverterBaseDescriptorSupplier {
    CurrencyConverterFileDescriptorSupplier() {}
  }

  private static final class CurrencyConverterMethodDescriptorSupplier
      extends CurrencyConverterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CurrencyConverterMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CurrencyConverterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CurrencyConverterFileDescriptorSupplier())
              .addMethod(getConvertMethod())
              .build();
        }
      }
    }
    return result;
  }
}
