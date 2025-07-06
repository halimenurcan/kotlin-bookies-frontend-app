import com.example.frontendbook.data.model.FollowerRequest
import com.example.frontendbook.data.model.FollowerResponse
import retrofit2.Response
import retrofit2.http.*

interface FollowersApiService {

    // Takipten çık (Composite Key: query parametre ile)
    @DELETE("followers")
    suspend fun deleteFollower(
        @Query("userId") userId: Long,
        @Query("followerId") followerId: Long
    ): Response<Unit>

    // Bireysel takip ilişkisi sorgula (Composite Key)
    @GET("followers")
    suspend fun getFollower(
        @Query("userId") userId: Long,
        @Query("followerId") followerId: Long
    ): Response<FollowerResponse?>

    // Belirli kullanıcının tüm takipçileri (yani profilime bakanlar)
    @GET("followers/{id}")
    suspend fun getFollowersOfUser(
        @Path("id") userId: Long
    ): Response<List<FollowerResponse>>

    // Belirli kullanıcının takip ettiği tüm kullanıcılar (yani benim takip ettiklerim)
    @GET("followers/followed/{id}")
    suspend fun getFollowingOfUser(
        @Path("id") userId: Long
    ): Response<List<FollowerResponse>>

    // Takip et
    @POST("followers")
    suspend fun createFollower(
        @Body request: FollowerRequest
    ): Response<Unit>
}