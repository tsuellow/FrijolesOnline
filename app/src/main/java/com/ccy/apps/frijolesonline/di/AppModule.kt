package com.ccy.apps.frijolesonline.di

import android.content.Context
import androidx.navigation.NavHostController
import com.ccy.apps.frijolesonline.common.Constants
import com.ccy.apps.frijolesonline.data.remote.FirebaseDatabaseConnection
import com.ccy.apps.frijolesonline.data.remote.FirebaseStorageConnection
import com.ccy.apps.frijolesonline.data.repository.FrijolesRepositoryImpl
import com.ccy.apps.frijolesonline.domain.repository.FrijolesRepository
import com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases.GetLotDetailUseCase
import com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases.GetPricingDataUseCase
import com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases.LotDetailUseCases
import com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases.PublishOfferUseCase
import com.ccy.apps.frijolesonline.domain.use_cases.lot_list_use_cases.*
import com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases.CreateProduceFromMapUseCase
import com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases.GetProducePropertyMapUseCase
import com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases.PublishLotUseCases
import com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases.SubmitLotUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseConn(): FirebaseDatabaseConnection {
        return FirebaseDatabaseConnection(Constants.URL_FIREBASE)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageConn(): FirebaseStorageConnection {
        return FirebaseStorageConnection(Constants.URL_STORAGE)
    }

    @Provides
    @Singleton
    fun provideFrijolesRepository(firebaseDatabaseConnection: FirebaseDatabaseConnection):FrijolesRepository{
        return FrijolesRepositoryImpl(firebaseDatabaseConnection)
    }

    @Provides
    @Singleton
    fun provideLotListUseCases(repository: FrijolesRepository): LotListUseCases {
        return LotListUseCases(
            getLotsUseCase = GetLotsUseCase(repository),
            getFilterArrayUseCase = GetFilterArrayUseCase(),
            getOrderingsUseCase = GetOrderingsUseCase(),
            reorderAndFilterUseCase = ReorderAndFilterUseCase()

        )
    }

    @Provides
    @Singleton
    fun providePublishLotUseCases(repository: FrijolesRepository): PublishLotUseCases {
        return PublishLotUseCases(
            getProducePropertyMapUseCase = GetProducePropertyMapUseCase(),
            createProduceFromMapUseCase = CreateProduceFromMapUseCase(),
            submitLotUseCase = SubmitLotUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLotDetailUseCases(repository: FrijolesRepository): LotDetailUseCases {
        return LotDetailUseCases(
            getLotDetailUseCase = GetLotDetailUseCase(repository),
            getPricingDataUseCase = GetPricingDataUseCase(repository),
            publishOfferUseCase = PublishOfferUseCase(repository),
            createProduceFromMapUseCase = CreateProduceFromMapUseCase()
        )
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object NavigationModule {
    @Provides
    fun providePublishLotNavController(@ApplicationContext context: Context): NavHostController {
        return NavigationService(context).navController}

//    @Provides
//    @ViewModelScoped
//    fun providePublishLotAnimatedNavController(@ApplicationContext context: Context): NavHostController {
//        return AnimatedNavigationService(context).animatedNavController}
}