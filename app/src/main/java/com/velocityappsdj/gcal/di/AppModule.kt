package com.velocityappsdj.gcal.di

import android.app.Application
import android.content.Context
import com.velocityappsdj.gcal.local.DataStoreUtil
import com.velocityappsdj.gcal.network.api.LoginApiService
import com.velocityappsdj.gcal.network.api.AuthInterceptor
import com.velocityappsdj.gcal.network.api.EventsAPIService
import com.velocityappsdj.gcal.network.api.OkhttpAuthenticator
import com.velocityappsdj.gcal.repo.EventsRepo
import com.velocityappsdj.gcal.repo.EventsRepoImpl
import com.velocityappsdj.gcal.repo.LoginRepo
import com.velocityappsdj.gcal.repo.LoginRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        const val UNAUTHENTICATED = "unauthenticated"
        const val AUTHENTICATED = "authenticated"
    }

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }

    @Provides
    @Singleton
    @Named(UNAUTHENTICATED)
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @Singleton
    @Named(AUTHENTICATED)
    fun providesOkHttpClientAuthenticated(
        authInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        okhttpAuthenticator: OkhttpAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor).authenticator(okhttpAuthenticator).build()
    }

    @Provides
    @Singleton
    @Named("")
    fun providesApplication(@ApplicationContext context: Context): Application {
        return context as Application
    }

    @Provides
    @Singleton
    @Named(AUTHENTICATED)
    fun providesRetrofitAuthenticated(@Named(AUTHENTICATED) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://www.googleapis.com/").client(okHttpClient)
            .build()

    }

    @Provides
    @Singleton
    @Named(UNAUTHENTICATED)
    fun providesRetrofitUnauthenticated(@Named(UNAUTHENTICATED) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.googleapis.com/oauth2/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()
    }


    @Provides
    @Singleton
    fun providesLoginApiService(@Named(UNAUTHENTICATED) retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesEventsAPIService(@Named(AUTHENTICATED) retrofit: Retrofit): EventsAPIService {
        return retrofit.create(EventsAPIService::class.java)
    }

    @Provides
    @Singleton
    fun providesDataStoreUtil(@ApplicationContext context: Context): DataStoreUtil {
        return DataStoreUtil(context)
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor(dataStoreUtil: DataStoreUtil): Interceptor {
        return AuthInterceptor(dataStoreUtil)
    }

    @Provides
    @Singleton
    fun providesLoginRepo(
        loginApiService: LoginApiService, dataStoreUtil: DataStoreUtil
    ): LoginRepo {
        return LoginRepoImpl(loginApiService = loginApiService, dataStoreUtil = dataStoreUtil)
    }

    @Provides
    @Singleton
    fun providesEventsRepo(eventsAPIService: EventsAPIService): EventsRepo {
        return EventsRepoImpl(eventsAPIService)
    }

    @Provides
    @Singleton
    fun providesAuthenticator(
        dataStoreUtil: DataStoreUtil,
        loginRepo: LoginRepo
    ): OkhttpAuthenticator {
        return OkhttpAuthenticator(dataStoreUtil = dataStoreUtil, loginRepo = loginRepo)
    }


}