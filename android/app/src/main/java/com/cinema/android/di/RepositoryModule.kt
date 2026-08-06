package com.cinema.android.di

import com.cinema.android.data.repository.AuthRepositoryImpl
import com.cinema.android.domain.repository.AuthRepository
import com.cinema.android.data.repository.MovieRepositoryImpl
import com.cinema.android.domain.repository.MovieRepository
import com.cinema.android.data.repository.ShowtimeRepositoryImpl
import com.cinema.android.domain.repository.ShowtimeRepository
import com.cinema.android.data.repository.BookingRepositoryImpl
import com.cinema.android.domain.repository.BookingRepository
import com.cinema.android.data.repository.PaymentRepositoryImpl
import com.cinema.android.domain.repository.PaymentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
    @Binds
    @Singleton
    abstract fun bindShowtimeRepository(
        showtimeRepositoryImpl: ShowtimeRepositoryImpl
    ): ShowtimeRepository
    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository
    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository
}