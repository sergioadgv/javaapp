package com.example.draganddrop.network;

import com.example.draganddrop.model.bookletModel.BookLetModel;
import com.example.draganddrop.model.change_pass_model.ChangePassModel;
import com.example.draganddrop.model.code_verify_model.VerifyModel;
import com.example.draganddrop.model.my_puzzle_model.MyPuzzleModel;
import com.example.draganddrop.model.offer_model.OfferModel;
import com.example.draganddrop.model.page_model.PageModel;
import com.example.draganddrop.model.puzzle_image_model.PuzzleImageModel;
import com.example.draganddrop.model.puzzle_model.PuzzleModel;
import com.example.draganddrop.model.quens_model.QuensModel;
import com.example.draganddrop.model.quiz_model.QuizModel;
import com.example.draganddrop.model.splash_details_model.SplashDetailsModel;
import com.example.luckydraw.model.UserModel;
import com.example.luckydraw.model.add_user_response.AddUserResponse;
import com.example.luckydraw.model.registerUserModel.RegisterUserModelItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("login_user.php")
    Call<UserModel> userLogin(@Field("u_name") String userName, @Field("pass") String pass);


    @FormUrlEncoded
    @POST("register-user.php")
    Call<AddUserResponse> addUser(
            @Field("user_name") String userName,
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("country") String country
    );


    @GET("ger-register-user.php")
    Call<ArrayList<RegisterUserModelItem>> getRegisterUser();



    @FormUrlEncoded
    @POST("get-quiz.php")
    Call<QuizModel> getQuiz(@Field("user_id") String userId,@Field("lan") String lan);

    @FormUrlEncoded
    @POST("virify-code.php")
    Call<VerifyModel> virifyCode(@Field("user_id") String userId, @Field("code") String code, @Field("type")String type, @Field("type_id")String type_code,@Field("name")String userName);

    @FormUrlEncoded
    @POST("get-questions.php")
    Call<QuensModel> getQuens(@Field("quiz_id") String quiz_id,@Field("lan") String lan);

    @FormUrlEncoded
    @POST("submit_ans.php")
    Call<VerifyModel> submitAns(@Field("user_id") String userId,
                                @Field("user_name")String user_name,
                                @Field("quiz_id")String quizId,
                                @Field("quens_id") String quensId,
                                @Field("ans") String ans,
                                @Field("lan") String lan,
                                @Field("is_win")String isWin
    );


    @FormUrlEncoded
    @POST("get-puzzles.php")
    Call<PuzzleModel> getAllPuzzle(@Field("user_id")String user_id);

    @FormUrlEncoded
    @POST("add-puzzle-my-collection.php")
    Call<VerifyModel> addPuzzleMyCollection(@Field("puzzle_id") String puzzle_id,@Field("user_id") String user_id,@Field("puzzle_name") String puzzle_name,@Field("puzzle_image") String puzzle_image);

    @FormUrlEncoded
    @POST("get-my-puzzles.php")
    Call<MyPuzzleModel> getMyPuzzles(@Field("user_id")String user_id);

    @FormUrlEncoded
    @POST("get-puzzle-image.php")
    Call<PuzzleImageModel> getPuzzleImage(@Field("puzzle_id")String puzzle_id,@Field("lan") String lanCode);

    @FormUrlEncoded
    @POST("get-page.php")
    Call<PageModel> getPages(@Field("b_id") String bId);

    @FormUrlEncoded
    @POST("add-booklet.php")
    Call<VerifyModel> addBooklet(@Field("user_id")String userId,@Field("booklet_id") String bookletId);

    @FormUrlEncoded
    @POST("ck-booklet.php")
    Call<VerifyModel> checkBooklet(@Field("user_id")String userId,@Field("booklet_id") String bookletId);

    @GET("get-offer.php")
    Call<OfferModel> getOffer();

    @FormUrlEncoded()
    @POST("user_activity.php")
    Call<VerifyModel> addUserActivity(@Field("page_name") String pageName,
                                      @Field("user_id") String userId,
                                      @Field("user_name") String userName,
                                      @Field("location")String location,
                                      @Field("lat")String lat,
                                      @Field("lng") String lng,
                                      @Field("season_code") String code,
                                      @Field("time")String time,
                                      @Field("country") String country,
                                      @Field("type_name")String typeName
    );

    @FormUrlEncoded()
    @POST("get-booklet.php")
    Call<BookLetModel> getBooklet(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("update_my_score_puzzle.php")
    Call<String> updateScore(@Field("user_id") String userId,@Field("score") String score,@Field("puzzle_id") String puzzleId);

    @FormUrlEncoded()
    @POST("reset_pass.php")
    Call<ChangePassModel> changePass(@Field("email") String email);

    @FormUrlEncoded()
    @POST("change_pass.php")
    Call<String> changePassByEmail(@Field("email") String email,@Field("pass") String pass);
    @GET("splash-details.php")
    Call<SplashDetailsModel> getSplashDetails();


}
