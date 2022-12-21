package com.example.technews.di

import android.content.Context
import com.example.technews.data.HeadlinesRepository
import com.example.technews.data.SearchRepository
import com.example.technews.data.SourcesRepository
import com.example.technews.data.local.ArticlesDao
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
        articlesDao: ArticlesDao,
        @ApplicationContext context: Context
    ) : HeadlinesRepository = HeadlinesRepository(newsApi, articlesDao, context)

    @Provides
    @ViewModelScoped
    fun provideSourcesRepository(
        newsApi: NewsApi,
        @ApplicationContext context: Context
    ): SourcesRepository = SourcesRepository(newsApi, context)

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        newsApi: NewsApi,
        @ApplicationContext context: Context
    ): SearchRepository = SearchRepository(newsApi, context)
}