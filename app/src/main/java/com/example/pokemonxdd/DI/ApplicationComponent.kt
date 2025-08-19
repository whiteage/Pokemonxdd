package com.example.pokemonxdd.DI

import android.content.Context
import androidx.room.Room
import com.example.pokemonxdd.data.RepositoryImpl
import com.example.pokemonxdd.data.room.DAO
import com.example.pokemonxdd.data.room.MainDB
import com.example.pokemonxdd.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationComponent {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext appContext: Context) : MainDB{
        return Room.databaseBuilder(
            appContext,
            MainDB::class.java,
            "courses.db").build()

    }
    @Provides
    fun provideUserDao(db: MainDB): DAO {
        return db.pokemonDao()}

    @Provides
    @Singleton
    fun provideRepo(dao: DAO) : Repository{
        return RepositoryImpl(dao = dao)

    }



}