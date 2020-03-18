package com.livefree.merchant.ui.network;

import com.livefree.merchant.ui.about.model.*;
import com.livefree.merchant.ui.booking.model.BookingRequest;
import com.livefree.merchant.ui.booking.model.BookingResponse;
import com.livefree.merchant.ui.booking.model.FoodOrderRequest;
import com.livefree.merchant.ui.booking.model.FoodOrderResponse;
import com.livefree.merchant.ui.businessday.model.BusinessDaysUpdateRequest;
import com.livefree.merchant.ui.businessday.model.BusinessDaysUpdateResponse;
import com.livefree.merchant.ui.businessday.model.BusinessDisplayRequest;
import com.livefree.merchant.ui.businessday.model.BusinessDisplayResponse;
import com.livefree.merchant.ui.location.model.CategoryListResponse;
import com.livefree.merchant.ui.location.model.LocationRequest;
import com.livefree.merchant.ui.location.model.LocationResponse;
import com.livefree.merchant.ui.login.model.CategoryResponse;
import com.livefree.merchant.ui.login.model.LoginRequest;
import com.livefree.merchant.ui.login.model.LoginResponse;
import com.livefree.merchant.ui.logout.model.LogOutResponse;
import com.livefree.merchant.ui.menu.model.*;
import com.livefree.merchant.ui.section.model.*;
import com.livefree.merchant.ui.password.changePasword.model.ChangeRequest;
import com.livefree.merchant.ui.password.changePasword.model.ChangeResponse;
import com.livefree.merchant.ui.password.forgot.model.ForgotRequest;
import com.livefree.merchant.ui.password.forgot.model.ForgotResponse;
import com.livefree.merchant.ui.password.otp.model.OtpRequest;
import com.livefree.merchant.ui.password.otp.model.OtpResponse;
import com.livefree.merchant.ui.password.otp.model.ResendResponse;
import com.livefree.merchant.ui.password.otp.model.ResentRequest;
import com.livefree.merchant.ui.server.model.*;
import com.livefree.merchant.ui.settings.model.*;
import com.livefree.merchant.ui.signUp.model.SignupRequest;
import com.livefree.merchant.ui.signUp.model.SignupResponse;
import com.livefree.merchant.ui.table.model.*;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestService {

    //SignUp Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/register")
    Single<Response<SignupResponse>> signup(@Body SignupRequest request);

    //Login Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/signin")
    Single<Response<LoginResponse>> login(@Body LoginRequest request);

    //OTP Forget Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/otpforgotpassword")
    Single<Response<ForgotResponse>> forgot(@Body ForgotRequest request);

    //Resend OTP Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/resendotp")
    Single<Response<ResendResponse>> ResendOtp(@Body ResentRequest request);


    //OTP Verify Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/confirmation")
    Single<Response<OtpResponse>> verifyOTP(@Body OtpRequest request);

    //Forgot Password Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/forgotpassword")
    Single<Response<ChangeResponse>> ChangePassword(@Body ChangeRequest request);


    //Get Profile Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/getProfile")
    Single<Response<ProfileResponse>> getProfile(@Header("token") String token);

    //Change Password Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/changepassword")
    Single<Response<ChangePassResponse>> ProfileChangePassword(@Header("token") String token, @Body ChangePassRequest request);

    //Choose Category List Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displaycat")
    Single<Response<CategoryListResponse>> categoryList(@Header("token") String token);

    // Save Category List Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/addcattag")
    Single<Response<LocationResponse>> saveCategory(@Header("token") String token, @Body LocationRequest request);


    // Add About Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/addcattagabout")
    Single<Response<AboutResponse>> saveAddAbout(@Header("token") String token, @Body AboutRequest request);

    // Display About Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displaycattagabout")
    Single<Response<DisplayAboutResponse>> DisplayAbout(@Header("token") String token, @Body DisplayAboutRequest request);

    // Display About Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/updatecattagabout")
    Single<Response<UpdateAboutResponse>> UpdateAbout(@Header("token") String token, @Body UpdateAboutRequest request);

    // Add Server  Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/addservers")
    Single<Response<AddServerResponse>> addServer(@Header("token") String token, @Body AddServerRequest request);

    // Category List Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displaycattag")
    Single<Response<CategoryResponse>> getCategory(@Header("token") String token);

    // Server List Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displayservers")
    Single<Response<ServerListResponse>> serverList(@Header("token") String token, @Body ServerListRequest request);

    // Add Section View Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/addsection")
    Single<Response<AddSectionResponse>> addSection(@Header("token") String token, @Body AddSectionRequest request);

    // Section List Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displaysection")
    Single<Response<SectionListResponse>> sectionList(@Header("token") String token, @Body SectionListRequest request);

    // Add Menu  Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/addmenu")
    Single<Response<AddMenuResponse>> addMenu(@Header("token") String token, @Body AddMenuRequest request);


    // Show Menu List Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displaymenu")
    Single<Response<MenuListResponse>> menuList(@Header("token") String token, @Body MenuListRequest request);


    // Add Table Detail  Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/addtableset")
    Single<Response<AddTableResponse>> addTable(@Header("token") String token, @Body AddTableRequest request);


    // Show Table Detail After Section  Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displaytableset")
    Single<Response<TableListResponse>> tableList(@Header("token") String token, @Body TableListRequest request);


    // Add Business Detail  Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/addbussinessday")
    Single<Response<BusinessDaysUpdateResponse>> addBusiness(@Header("token") String token, @Body BusinessDaysUpdateRequest request);

    //Display Business Details Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/displaybusinessday")
    Single<Response<BusinessDisplayResponse>> displayBusinessDay(@Header("token") String token, @Body BusinessDisplayRequest request);

    //Edit Server Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/updateservers")
    Single<Response<UpdateServerResponse>> updateServer(@Header("token") String token, @Body UpdateServerRequest request);


    // Delete Server Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/deleteserver")
    Single<Response<DeleteServerResponse>> deleteServer(@Header("token") String token, @Body DeleteServerRequest request);

    //Edit Section Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/updatesection")
    Single<Response<UpdateSectionResponse>> updateSection(@Header("token") String token, @Body UpdateSectionRequest request);


    //Delete ection Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/deletesection")
    Single<Response<DeleteSectionResponse>> deleteSection(@Header("token") String token, @Body DeleteSectionRequest request);


    //Edit Menu Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/updatemenu")
    Single<Response<UpdateMenuResponse>> updateMenu(@Header("token") String token, @Body UpdateMenuRequest request);


    //Delete Menu Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/deletemenu")
    Single<Response<DeleteMenuResponse>> deleteMenu(@Header("token") String token, @Body DeleteMenuRequest request);


    //Edit Table Detail Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/edittableset")
    Single<Response<UpdateTableListResponse>> updateTable(@Header("token") String token, @Body UpdateTableListRequest request);

    // Table Delete List Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/deletetableset")
    Single<Response<DeleteTableListResponse>> deleteTable(@Header("token") String token, @Body DeletetTableListdataRequest request);


    //My Booking List
    @Headers({"content-type: application/json"})
    @POST("/merchant/bookinglist")
    Single<Response<BookingResponse>> bookingList(@Header("token") String token, @Body BookingRequest request);


    //Edit profile Screen
    @Headers({"content-type: application/json"})
    @POST("/merchant/editProfile")
    Single<Response<EditResponse>> editProfile(@Header("token") String token, @Body EditRequest request);


    //Edit Single Server List
    @Headers({"content-type: application/json"})
    @POST("/merchant/singleserver")
    Single<Response<SingleServerResponse>> singleServer(@Header("token") String token, @Body SingleServerRequest request);


    //Edit Single Table List
    @Headers({"content-type: application/json"})
    @POST("/merchant/singletableset")
    Single<Response<SingleSectionResponse>> singleTable(@Header("token") String token, @Body SingleSectionRequest request);

    //Edit Single Menu List
    @Headers({"content-type: application/json"})
    @POST("/merchant/singlemenu")
    Single<Response<SingleMenuResponse>> singleMenu(@Header("token") String token, @Body SingleMenuRequest request);

    //Oder Food List in Booking
    @Headers({"content-type: application/json"})
    @POST("/merchant/bookingMenu")
    Single<Response<FoodOrderResponse>> bookingMenu(@Header("token") String token, @Body FoodOrderRequest request);

    //LogOut
    @Headers({"content-type: application/json"})
    @POST("/merchant/merchantLogout")
    Single<Response<LogOutResponse>> logOut(@Header("token") String token);
}
