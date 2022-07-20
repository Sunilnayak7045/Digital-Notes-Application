# Digital-Notes-Application-Hilt


dependencies : implementation 'com.squareup.okhttp3:okhttp:4.9.3'


Add a header dynamically:
For example Shared Preference Token

Method 1:
-> add header in every request

A request Header can be updated dynamically using the @Header annotation. A corresponding parameter must be provided to the @Header. 

If the value is null, the header will be omitted. Otherwise, toString will be called on the value, and the result used.


@GET("user")
 
Call<User> getUser(@Header("Authorization") String authorization)


Method 2:
-> suppose we have 50 post/get request in which header is used,
adding header in every request is tedious way, 
alternatively we can use "OkHttp interceptor"

Headers that needs to be added to every request can be specified using an OkHttp interceptor.
In simple words OkHttp interceptor is interceptor will add header in a request before post/get request is send



UserApi doesnot need token in header
NotesApi needs token in header

Method 1: without interceptor

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

Method 2: with interceptor

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }


    @Singleton
    @Provides
    fun providesAuthRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


Writing 2 different method is tedious, so use common method
in which return type will be Retrofit.Builder (i.e builder object)
not Retrofit.

Before:

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }


    //Retrofit obj that we are getting in providesUserAPI(retrofitBuilder: Retrofit), is coming from providesRetrofit()
    //UserApi doesn't need token, so we can use providesRetrofit in this interceptor is not used
    //NotesApi needs token, so use provideOkHttpClient

After:

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NoteAPI {
        return retrofitBuilder.client(okHttpClient).build().create(NoteAPI::class.java)
    }




