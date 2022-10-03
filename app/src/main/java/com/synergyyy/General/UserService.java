
package com.synergyyy.General;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergyyy.EquipmentSearch.CheckListAddRequest;
import com.synergyyy.EquipmentSearch.GetPmTaskItemsResponse;
import com.synergyyy.EquipmentSearch.GetUpdatePmTaskRequest;
import com.synergyyy.EquipmentSearch.GetUpdatePmTaskResponse;
import com.synergyyy.Messages.MessageObject;
import com.synergyyy.Messages.MessageResponse;
import com.synergyyy.Messages.SingleMessageResponse;
import com.synergyyy.Search.AcceptRejectBody;
import com.synergyyy.Search.BeforeImageResponse;
import com.synergyyy.Search.CloseFaultReportBody;
import com.synergyyy.Search.EquipmentGeoLocationClass;
import com.synergyyy.Search.fmm;
import com.synergyyy.Search.LocationScanModel;
import com.synergyyy.Search.PauseRequestBody;
import com.synergyyy.Search.SearchResposeWithLatLon;
import com.synergyyy.SearchTasks.DeleteImageRequest;
import com.synergyyy.SearchTasks.DeleteTaskImageRequest;
import com.synergyyy.SearchTasks.GetCheckListResponse;
import com.synergyyy.SearchTasks.TaskSearchResponse;
import com.synergyyy.EquipmentSearch.UploadImageRequest;
import com.synergyyy.FaultReport.CreateFaultRequestPojo;
import com.synergyyy.FaultReport.SelectTechnicianResponse;
import com.synergyyy.FaultReport.UploadPictureRequest;
import com.synergyyy.Otp.OtpRequest;
import com.synergyyy.Otp.ResendOtpRequest;
import com.synergyyy.Search.EditFaultReportRequest;
import com.synergyyy.Search.EquipmentSearchResponseforEdit;
import com.synergyyy.Search.SearchResponse;
import com.synergyyy.Search.UploadFileRequest;
import com.synergyyy.SearchTasks.UploadTaskImageResponse;
import com.synergyyy.SearchTasks.TaskResponse;
import com.synergyyy.UploadPdf.AcceptRejectQuotationMoldel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.PUT;

public interface UserService {
    //login to user account
    @POST("authenticate")
    @Headers("Content-Type: application/json")
    Call<UserResponse> saveUser(@Body UserRequest userRequest);

    //otp call
    @POST("verify2fa")
    @Headers("Content-Type: application/json")
    Call<UserResponse> callOtp(@Body OtpRequest otpRequest);

    //resend otp call
    @POST("resendCode")
    @Headers("Content-Type: application/json")
    Call<Void> callResendOtp(@Body ResendOtpRequest resendOtpRequest);

    //generate workspace
    @GET("workspaces")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getWorkspace(@Header("Authorization") String token);

    //gen dep for fault
    @GET("general/departments/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDep(@Header("workspaceId") String workspaceId
            , @Header("Authorization") String token
            , @Path("workspaceIdPath") String workspaceIdPath);

    //gen priority for fault
    @GET("general/priorty/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenproirity(@Header("Authorization") String token,
                                   @Path("workspaceIdPath") String workspaceIdPath);


    //gen division for fault
    @GET("general/divisions/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDivisions(@Header("Authorization") String token
            , @Path("workspaceIdPath") String workspaceIdPath);

    //get building for fault

    @GET("general/buildings/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenBuildings(@Header("Authorization") String token,
                                    @Path("workspaceIdPath") String workspaceIdPath);

    //get fayult caTegories for fault
    @GET("general/faultCategories/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenFaultCat(@Header("Authorization") String token,
                                   @Path("workspaceIdPath") String workspaceIdPath);

    //get fault maint grp
    @GET("general/maintenanceGrpCategory/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenMaintGrp(@Header("Authorization") String token,
                                   @Path("workspaceIdPath") String workspaceIdPath);

    //get fault location grp
    @GET("general/locations/{buildId}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenLocation(@Header("Authorization") String token,
                                   @Path("buildId") Integer buildId);


    //get fault equipment list
    @GET("general/building/{buildId}/location/{locId}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenEquip(@Header("Authorization") String token,
                                @Header("role") String role,
                                @Header("workspace") String workspace,
                                @Path("buildId") Integer buildId,
                                @Path("locId") Integer locId);

    //create fault
    @POST("faultreport")
    @Headers("Content-Type: application/json")
    Call<JsonObject> createFault(@Body CreateFaultRequestPojo createFaultRequestPojo,
                                 @Header("workspace") String workspace,
                                 @Header("Authorization") String token
            , @Header("role") String role);

    //get search
    @GET("faultreport/search")
    @Headers("Content-Type: application/json")
    Call<List<SearchResponse>> getSearchResult(@Header("workspace") String dynamicWorkSpace,
                                               @Query("query") String param,
                                               @Query("type") String type,
                                               @Header("Authorization") String token,
                                               @Header("role") String role);


    //equipment search tasks
    @GET("task/equipment/{equipmentCode}/{dueOrOverdue}")
    @Headers("Content-Type: application/json")
    Call<List<GetPmTaskItemsResponse>> getEquipmentTask(@Path("equipmentCode") String path,
                                                        @Path("dueOrOverdue") String dueOrOverdue,
                                                        @Header("role") String role,
                                                        @Header("Authorization") String token,
                                                        @Header("workspace") String workspace);


    //getEquipment Scan code
    @GET("task/{equipmentCode}/{status}")
    @Headers("Content-Type: application/json")
    Call<List<TaskResponse>> getTaskOnQrList(@Path("equipmentCode") String path,
                                             @Path("status") String status,
                                             @Header("Authorization") String token);


    //pm View Task
    @GET("task/{id}")
    @Headers("Content-Type: application/json")
    Call<GetPmTaskItemsResponse> getCallPmTask(@Path("id") String id,
                                               @Header("Authorization") String token,
                                               @Header("role") String role);

    //Update pm task
    @PUT("task/updateTask")
    @Headers("Content-Type: application/json")
    Call<GetUpdatePmTaskResponse> postPmTaskUpdate(@Body GetUpdatePmTaskRequest getUpdatePmTaskRequest,
                                                   @Header("Authorization") String token,
                                                   @Header("role") String role,
                                                   @Header("workspace") String workspace);

    //checklistActivityView
    @GET("task/{path}/checklist")
    @Headers("Content-Type: application/json")
    Call<List<GetCheckListResponse>> getChecklist(@Path("path") String path,
                                                  @Header("Authorization") String token);

    //checkListActivitiesSave
    @POST("task/updateChecklists")
    @Headers("Content-Type: application/json")
    Call<List<CheckListAddRequest>> postCheckList(@Body List<CheckListAddRequest> checkListAddRequest,
                                                  @Header("Authorization") String token);


    //to get the technician list
    @GET("general/technicians")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getTechnicianCall(@Header("Authorization") String token,
                                      @Header("workspace") String workspace);

    //upload image

    @POST("faultreport/{value}image")
    @Headers("Content-Type: application/json")
    Call<Void> uploadCaptureImage(@Path("value") String value,
                                  @Header("Authorization") String token,
                                  @Header("workspace") String workspace,
                                  @Body UploadPictureRequest uploadPictureRequest);

    // http://192.168.2.18:8081/api/equip/search?query=e
    @GET("equip/search?")
    @Headers("Content-Type: application/json")
    Call<List<EquipmentSearchResponseforEdit>> getSearchEquipment(@Query("query") String query,
                                                                  @Header("Authorization") String token,
                                                                  @Header("workspace") String workspace,
                                                                  @Header("role") String role);

    //update
    @PUT("faultreport")
    @Headers("Content-Type: application/json")
    Call<Void> updateReport(@Body EditFaultReportRequest editFaultReportRequest,
                            @Header("Authorization") String token,
                            @Header("workspace") String workspace,
                            @Header("role") String role);

    @GET("faultreport/{value}image/{frId}")
    @Headers("Content-Type: application/json")
    Call<List<BeforeImageResponse>> getBeforeImage(@Path("value") String value,
                                                   @Path("frId") String frId,
                                                   @Header("workspace") String workspace,
                                                   @Header("Authorization") String token);

    //call alreaddy images
    @GET("faultreport/getimage/{imageName}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getImageBase64(@Path("imageName") String imagename,
                                      @Header("workspace") String workspace,
                                      @Header("Authorization") String token);


    //enable notification
    @GET("ws/dr/not/{zero}/{path}")
    @Headers("Content-Type: application/json")
    Call<Void> getNotification(@Path("zero") String zeroOrOne,
                               @Path("path") String devicetoken,
                               @Header("Authentication") String token,
                               @Header("workspace") String workspace);


    //Logout user
    @POST("logout?")
    @Headers("Content-Type: application/json")
    Call<Void> logoutUser(@Query("deviceToken") String query,
                          @Header("Authorization") String token);

    //imageUploadTask
    @POST("task/{afterimage}image")
    @Headers("Content-Type: application/json")
    Call<UploadTaskImageResponse> taskImageUpload(@Header("role") String role,
                                                  @Header("Authorization") String token,
                                                  @Path("afterimage") String imageValue,
                                                  @Body UploadImageRequest uploadImageRequest);

    //search tasks
    @GET("task/search")
    @Headers("Content-Type: application/json")
    Call<List<TaskSearchResponse>> taskSearch(@Query("query") String query,
                                              @Query("type") String type,
                                              @Header("Authorization") String token,
                                              @Header("role") String role,
                                              @Header("workspace") String workspace);

    //Task image
    @GET("task/getimage/{imageName}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getTaskImage(@Path("imageName") String imageName,
                                    @Header("role") String role,
                                    @Header("Authorization") String token,
                                    @Header("workspace") String workspace);


    //get task signature
    @GET("task/acksignature/{signame}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getSignature(@Path("signame") String imageName,
                                    @Header("role") String role,
                                    @Header("Authorization") String token,
                                    @Header("workspace") String workspace);

    //select technician
    @GET("general/techniciansearch/?")
    @Headers("Content-Type: application/json")
    Call<SelectTechnicianResponse> getTechnicianList(@Query("query") String query,
                                                     @Header("Authorization") String token,
                                                     @Header("workspace") String workspace,
                                                     @Header("role") String role);

    //pause request
    @POST("faultreport/pauserequest")
    @Headers("Content-Type: application/json")
    Call<Void> getRequestPause(@Header("Authorization") String token,
                               @Header("workspace") String workspace,
                               @Body PauseRequestBody pauseRequestBody
    );

    //pause Accept
    @POST("faultreport/pauserequest/accept")
    @Headers("Content-Type: application/json")
    Call<Void> getAccept(@Header("Authorization") String token,
                         @Header("workspace") String workspace,
                         @Body AcceptRejectBody acceptRejectBody
    );

    //pause reject
    @POST("faultreport/pauserequest/reject")
    @Headers("Content-Type: application/json")
    Call<Void> getReject(@Header("Authorization") String token,
                         @Header("workspace") String workspace,
                         @Body AcceptRejectBody acceptRejectBody);

    @POST("faultreport/findOne")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getFindOne(@Header("WorkspaceId") String workspaceId,
                                @Header("Authorization") String token,
                                @Header("role") String role,
                                @Body SearchResposeWithLatLon searchResposeWithLatLon);


    @POST("faultreport/equipment")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getEquipmentDetailsOnGeolocation(@Header("WorkspaceId") String workspaceId,
                                                      @Header("Authorization") String token,
                                                      @Header("role") String role,
                                                      @Body EquipmentGeoLocationClass geoLocationClass
    );


    // new messageslist faizan
    @GET("msg/messages")
    @Headers("Content-Type: application/json")
    Call<MessageObject> getListOfMessages(@Header("Authorization") String token,
                                          @Header("username") String username);

    //send message read status faizan
    @GET("msg/{id}")
    @Headers("Content-Type: application/json")
    Call<SingleMessageResponse> checkReadMessage(@Header("Authorization") String token,
                                                 @Path("id") String id);

    //Get Message List
    @GET("msg/")
    @Headers("Content-Type: application/json")
    Call<List<MessageResponse>> getMessageList(@Header("Authorization") String token,
                                               @Header("username") String username);

    @GET("msg/type")
    @Headers("Content-Type: application/json")
    Call<List<MessageResponse>> getChatList(@Header("Authorization") String token,
                                            @Header("username") String username,
                                            @Query("type") String type);


    // get search for quote upload

    @GET("faultreport/quotationupload/search")
    @Headers("Content-Type: application/json")
    Call<List<SearchResponse>> getsearchForQuoteUpload(@Header("workspace") String dynamicWorkSpace,
                                                       @Query("query") String param,
                                                       // @Query("type") String type,
                                                       @Header("Authorization") String token,
                                                       @Header("role") String role);

    //search for purcghase order
    @GET("faultreport/purchaseOrderupload/search")
    @Headers("Content-Type: application/json")
    Call<List<SearchResponse>> getSearchForPurchase(@Header("workspace") String dynamicWorkSpace,
                                                    @Query("query") String param,
                                                    // @Query("type") String type,
                                                    @Header("Authorization") String token,
                                                    @Header("role") String role);

    //upload purchase order  /api/faultreport/purchaseOrder
    @POST("faultreport/purchaseOrder")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> uploadPurchasePdf(@Body UploadFileRequest uploadFileRequest,
                                         @Header("Authorization") String token,
                                         @Header("workspace") String workspace,
                                         @Header("role") String role);


    //upload quote file
    @POST("faultreport/quotationUpload")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> uploadFilePdf(@Body UploadFileRequest uploadFileRequest,
                                     @Header("Authorization") String token,
                                     @Header("workspace") String workspace,
                                     @Header("role") String role);

    //GET available PDF quote file
    @GET("faultreport/quotation/{frId}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> searchForQuoatePdf(@Path("frId") String frId,
                                          @Header("Authorization") String token,
                                          @Header("workspace") String workspace,
                                          @Header("role") String role);

    //GET PDF quote file
    @GET("faultreport/purchaseOrder/{frId}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> searchForPurchasePdf(@Path("frId") String frId,
                                            @Header("Authorization") String token,
                                            @Header("workspace") String workspace,
                                            @Header("role") String role);

    //http://192.168.2.21:8081/api/faultreport/scan/location
    @POST("faultreport/scan/location")
    @Headers("Content-Type: application/json")
    Call<Void> checkLocation(@Header("Authorization") String token,
                             @Header("workspace") String workspace,
                             @Header("role") String role,
                             @Body LocationScanModel locationScanModel);


    //http://192.168.2.21:8081/api/faultreport/scan/location
    @POST("faultreport/quotation/status")
    @Headers("Content-Type: application/json")
    Call<Void> quotationAccept(@Header("Authorization") String token,
                               @Header("workspace") String workspace,
                               @Header("role") String role,
                               @Body AcceptRejectQuotationMoldel quotationMoldel);

    //delete image
    @POST("faultreport/delete/image")
    @Headers("Content-Type: application/json")
    Call<Void> postDeleteImage(@Body DeleteImageRequest deleteImageRequest,
                               @Header("workspace") String workspace,
                               @Header("Authorization") String token,
                               @Header("role") String role);


    @POST("task/delete/image")
    @Headers("Content-Type: application/json")
    Call<Void> getDeleteTaskImage(@Body DeleteTaskImageRequest deleteTaskImageRequest,
                                  @Header("workspace") String workspace,
                                  @Header("Authorization") String token,
                                  @Header("role") String role);


    @GET("faultreport/acksignature/{frId}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getSignatureAckFr(@Path("frId") String frId,
                                         @Header("workspace") String workspace,
                                         @Header("Authorization") String token,
                                         @Header("role") String role);

    //tech sign fault report
    @GET("faultreport/techsignature/{signame}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getTechnicianSign(@Path("signame") String frId,
                                         @Header("workspace") String workspace,
                                         @Header("Authorization") String token,
                                         @Header("role") String role);

    //tech sign task
    @GET("task/techsignature/{tech_signature}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getTechSignTask(@Path("tech_signature") String frId,
                                       @Header("workspace") String workspace,
                                       @Header("Authorization") String token,
                                       @Header("role") String role);

    @GET("faultreport/editcompleted/{frId}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> checkCompletefFridIsEditable(@Path("frId") String frId,
                                                    @Header("workspace") String workspace,
                                                    @Header("Authorization") String token,
                                                    @Header("role") String role);


    @POST("faultreport/closefault")
    @Headers("Content-Type: application/json")
    Call<Void> closeFaultFReport(@Body CloseFaultReportBody closeFaultReportBody,
                                 @Header("workspace") String workspace,
                                 @Header("Authorization") String token,
                                 @Header("role") String role);


    //reset password
    @GET("reset")
    @Headers("Content-Type: application/json")
    Call<Void> resetPassword(@Header("email") String email);

    //check link is available
    @GET("reset_password")
    @Headers("Content-Type: application/json")
    Call<Void> checkLinkAvailable(@Query("token") String token);

    //new password setup
    @POST("changePassword")
    @Headers("Content-Type: application/json")
    Call<Void> newPassword(@Body NewPasswordRequestBody newPasswordRequestBody);

    //reieve all fmms
    @GET("faultreport/fmmlist")
    @Headers("Content-Type: application/json")
    Call<List<fmm>> getAllFmm(@Header("Authorization") String token,
                              @Header("workspace") String workspace);


    //all fmms for tasks
    @GET("task/fmmlist")
    @Headers("Content-Type: application/json")
    Call<List<fmm>> getAllFmmPMTask(@Header("Authorization") String token,
                              @Header("workspace") String workspace);
}
