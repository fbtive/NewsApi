package com.example.technews.di

import android.content.Context
import com.example.technews.data.HeadlinesRepository
import com.example.technews.data.remote.API.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideHeadlinesRepository(
        newsApi: NewsApi,
        @ApplicationContext context: Context
    ) : HeadlinesRepository = HeadlinesRepository(newsApi, context)
}