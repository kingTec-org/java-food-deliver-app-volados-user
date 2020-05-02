package gofereats.dependencies.component;
/**
 * @package com.trioangle.gofereats
 * @subpackage dependencies.component
 * @category AppComponent
 * @author Trioangle Product Team
 * @version 1.0
 **/

import javax.inject.Singleton;

import dagger.Component;
import gofereats.adapters.main.FavouritesListAdapter;
import gofereats.adapters.main.FoodListAdapter;
import gofereats.adapters.main.MenuListAdapter;
import gofereats.adapters.main.PastOrderAdapter;
import gofereats.adapters.main.PromoDetailAdapter;
import gofereats.adapters.main.ReceiptOrderAdapter;
import gofereats.adapters.main.UpcomingAdapter;
import gofereats.adapters.main.home.FavoriteListAdapter;
import gofereats.adapters.main.home.MoreRestaurantListAdapter;
import gofereats.adapters.main.home.NewRestaurantListAdapter;
import gofereats.adapters.main.home.PopularListAdapter;
import gofereats.adapters.main.home.SeeMoreListAdapter;
import gofereats.adapters.main.home.UnderListAdapter;
import gofereats.adapters.main.home.ViewPagerAdapter;
import gofereats.backgroundtask.ImageCompressAsyncTask;
import gofereats.configs.RunTimePermission;
import gofereats.configs.SessionManager;
import gofereats.dependencies.module.AppContainerModule;
import gofereats.dependencies.module.ApplicationModule;
import gofereats.dependencies.module.NetworkModule;
import gofereats.pushnotification.MyFirebaseInstanceIDService;
import gofereats.pushnotification.MyFirebaseMessagingService;
import gofereats.utils.CommonMethods;
import gofereats.utils.DateTimeUtility;
import gofereats.utils.ImageUtils;
import gofereats.utils.LanguageConverter;
import gofereats.utils.RequestCallback;
import gofereats.utils.TermsWebView;
import gofereats.utils.WebServiceUtils;
import gofereats.views.main.MainActivity;
import gofereats.views.main.ShowDialog;
import gofereats.views.main.alertdialog.DialogActivity;
import gofereats.views.main.fragments.AccountFragment;
import gofereats.views.main.fragments.HomeFragment;
import gofereats.views.main.fragments.SearchFragment;
import gofereats.views.main.fragments.orders.OrderFragment;
import gofereats.views.main.fragments.orders.PastOrders;
import gofereats.views.main.fragments.orders.Upcoming;
import gofereats.views.main.fragments.sortingfragments.DietaryFragment;
import gofereats.views.main.fragments.sortingfragments.DietaryFragmentold;
import gofereats.views.main.fragments.sortingfragments.FilterFragment;
import gofereats.views.main.fragments.sortingfragments.PriceFragment;
import gofereats.views.main.fragments.sortingfragments.SortFragment;
import gofereats.views.main.subviews.AddCardActivity;
import gofereats.views.main.subviews.AddToCartActivity;
import gofereats.views.main.subviews.CancellationActivity;
import gofereats.views.main.subviews.ContactActivity;
import gofereats.views.main.subviews.DriverTrackingActivity;
import gofereats.views.main.subviews.EditProfileActivity;
import gofereats.views.main.subviews.EntryActivity;
import gofereats.views.main.subviews.FavouritesActivity;
import gofereats.views.main.subviews.LocActivity;
import gofereats.views.main.subviews.PaymentActivity;
import gofereats.views.main.subviews.PlaceOrderActivity;
import gofereats.views.main.subviews.PromoActivity;
import gofereats.views.main.subviews.RatingActivity;
import gofereats.views.main.subviews.RestaurantDetailActivity;
import gofereats.views.main.subviews.RestaurantInfoActivity;
import gofereats.views.main.subviews.SettingActivity;
import gofereats.views.main.subviews.wallet.AddWalletActivity;
import gofereats.views.signup_login.ForgotPasswordOtpActivity;
import gofereats.views.signup_login.LoginActivity;
import gofereats.views.signup_login.MobileActivity;
import gofereats.views.signup_login.RegisterActivity;
import gofereats.views.signup_login.ResetPassword;
import gofereats.views.splash.SplashActivity;


/*****************************************************************
 App Component
 ****************************************************************/
@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {
    // ACTIVITY

    void inject(SplashActivity splashActivity);

    void inject(FavouritesListAdapter favouritesListAdapter);

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);

    void inject(MobileActivity mobileActivity);

    void inject(ForgotPasswordOtpActivity forgotPasswordOtpActivity);

    void inject(ResetPassword resetPassword);

    void inject(MainActivity mainActivity);

    void inject(SettingActivity settingActivity);

    void inject(PromoActivity promoActivity);

    void inject(FavouritesActivity favouritesActivity);

    void inject(PaymentActivity paymentActivity);

    void inject(AddCardActivity addCardActivity);

    void inject(AddToCartActivity addToCartActivity);

    void inject(PlaceOrderActivity placeOrderActivity);

    void inject(RestaurantDetailActivity RestaurantDetailActivity);

    void inject(LocActivity locationActivity);

    void inject(DriverTrackingActivity driverTrackingActivity);

    void inject(RatingActivity ratingActivity);

    void inject(EntryActivity entryActivity);

    void inject(DialogActivity dialogActivity);

    void inject(CancellationActivity cancellationActivity);

    void inject(AddWalletActivity addWalletActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(ContactActivity contactActivity);

    void inject(RestaurantInfoActivity restaurantInfoActivity);

    void inject(ShowDialog showDialog);


    // Fragments
    void inject(AccountFragment accountFragment);

    void inject(HomeFragment homeFragment);

    void inject(SearchFragment searchFragment);

    void inject(OrderFragment orderFragment);

    void inject(PastOrders pastOrders);

    void inject(Upcoming upcoming);

    void inject(SortFragment sortFragment);

    void inject(PriceFragment priceFragment);

    void inject(DietaryFragment dietaryFragment);

    void inject(DietaryFragmentold dietaryFragmentold);

    void inject(FilterFragment filterFragment);

    // Utilities
    void inject(RunTimePermission runTimePermission);

    void inject(SessionManager sessionManager);

    void inject(ImageUtils imageUtils);

    void inject(CommonMethods commonMethods);


    void inject(RequestCallback requestCallback);

    void inject(DateTimeUtility dateTimeUtility);

    void inject(WebServiceUtils webServiceUtils);

    // Adapters
    void inject(MoreRestaurantListAdapter moreRestaurantListAdapter);

    void inject(FavoriteListAdapter favoriteListAdapter);

    void inject(PopularListAdapter popularListAdapter);

    void inject(UnderListAdapter underListAdapter);

    void inject(NewRestaurantListAdapter newRestaurantListAdapter);

    void inject(ViewPagerAdapter viewPagerAdapter);

    void inject(FoodListAdapter foodListAdapter);

    void inject(PastOrderAdapter pastOrderAdapter);

    void inject(UpcomingAdapter upcomingAdapter);

    void inject(ReceiptOrderAdapter receiptOrderAdapter);

    void inject(PromoDetailAdapter promoDetailAdapter);

    void inject(SeeMoreListAdapter seeMoreListAdapter);

    void inject(MenuListAdapter menuListAdapter);



   /* void inject(ChatConversationListAdapter chatConversationListAdapter);*/

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);


    // AsyncTask
    void inject(ImageCompressAsyncTask imageCompressAsyncTask);

    void inject(LanguageConverter languageConverter);

    void inject(TermsWebView termsWebView);


}
