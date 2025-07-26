import com.example.frontendbook.data.model.FollowerRequest
import com.example.frontendbook.data.model.FollowersResponse
import retrofit2.Response
import retrofit2.http.*

interface FollowersApiService {
    @POST("followers")
    suspend fun createFollower(@Body req: FollowerRequest): Response<Unit>

    @GET("followers/{userId}/{followerId}")
    suspend fun getFollower(
        @Path("userId") userId: Long,
        @Path("followerId") followerId: Long
    ): Response<FollowersResponse?>

    @DELETE("followers/{userId}/{followerId}")
    suspend fun deleteFollower(
        @Path("userId") userId: Long,
        @Path("followerId") followerId: Long
    ): Response<Unit>

    @GET("followers/{userId}")
    suspend fun getFollowersOfUser(@Path("userId") userId: Long): Response<FollowersResponse>

    @GET("followers/followed/{userId}")
    suspend fun getFollowingOfUser(@Path("userId") userId: Long): Response<FollowersResponse>

    @GET("followers/{userId}/count")
    suspend fun getFollowerCount(@Path("userId") userId: Long): Int

    @GET("followers/followed/{userId}/count")
    suspend fun getFollowingCount(@Path("userId") userId: Long): Int
}
