/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Controller.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class ReceiverHandler {

    private static LoginController logincontroller;
    private static NotificationController notificationcontroller;
    private static FriendsController friendscontroller;
    private static WishListController wishlistcontroller;
    private static SignupController signupcontroller;
    private static ItemsController itemscontroller;
    private static FriendProfileController friendprofilecontroller;
    private static PaymentController paymentcontroller;
    private static HomeController homecontroller;
    private boolean flag;
    private Gson gson;

    public ReceiverHandler() {
        gson = new Gson();
        process();
    }

    public void process() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (MyConnection.getStatus()) {
                    try {
                        String received = MyConnection.getInstance().getInputStream().readLine();
                        JsonObject client = gson.fromJson(received, JsonObject.class);
                        String type = client.get("reply").getAsString();
                        System.out.println(received);
                        MessageProtocol.RETRIEVAL retriev = gson.fromJson(type, MessageProtocol.RETRIEVAL.class);
                        MessageProtocol.MODIFY modify = gson.fromJson(type, MessageProtocol.MODIFY.class);

                        if (retriev != null) {
                            switch (retriev) {
                                case GET_FRIENDS:
                                    friendscontroller.getFriendListHandler(received);
                                    break;
                                case GET_WISHLIST: 
                                    wishlistcontroller.getWishListHandler(received);
                                    break;
                                case GET_FRIEND_WISHLIST :
                                    friendprofilecontroller.waitForHandler(received);
                                    break;
                                case GET_ITEMS:
                                    itemscontroller.getItemsListHandler(received);
                                    break;
                                case GET_CONTRIBUTION:
                                    wishlistcontroller.getContributionHandler(received);
                                    break;
                                case GET_NOTIFICATIONS:
                        /**/
                                    // Refresh when Receiving any notifications
                                    if(flag){
                                        int size  = notificationcontroller.numOfNotification();
                                        notificationcontroller.waitForHandler(received);
                                        homecontroller.createTabGraphic(size , notificationcontroller.numOfNotification()); 
                                        friendscontroller.addAndRemoveFriendHandler();
                                        wishlistcontroller.getWishlist();
                                    }else
                                    {
                                        notificationcontroller.waitForHandler(received);
                                        flag = true;
                                    }
                                    break;
                        /**/
                                case GET_USERS:
                                    friendscontroller.searchHandler(received);
                                    break;
                                case LOGIN:
                                    logincontroller.waitForHandler(received);
                                    break;
                            }
                        } else if (modify != null) {
                            switch (modify) {
                                case ADD_TO_WISHLIST:
                                    itemscontroller.addItemHandler(received);
                                    break;
                                case CONTRIBUTE:
                                    paymentcontroller.waitForHandler(received);
                                     break;
                                case REGISTER:
                                    signupcontroller.waitForHandler(received);
                                    break;
                                case ADD_FRIEND:
                                    friendscontroller.addAndRemoveFriendHandler();
                                    break;
                                case REMOVE_FRIEND:
                                    friendscontroller.addAndRemoveFriendHandler();
                                    break;
                                case ACCEPT_FRIEND :
                                    notificationcontroller.nodeHandler(received);
                                    friendscontroller.addAndRemoveFriendHandler();
                                    break;
                                case DENY_REQUEST :
                                    notificationcontroller.nodeHandler(received);
                                    friendscontroller.addAndRemoveFriendHandler();
                                    break;
                                case CLEAR_WISHLIST:
                                    wishlistcontroller.getWishlist();
                                    break;
                            }
                        }
                    } catch (IOException ex) {
                        if (!ex.getMessage().toLowerCase().equals("socket closed") & !ex.getMessage().toLowerCase().equals("stream closed") ) {
                            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        else if ( ex.getMessage().toLowerCase().equals("connection reset"))
                        {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("");
                                alert.setContentText("Server Disconnected .. Sorry we need to log you out :(");
                                alert.showAndWait();
                                Stage stage = (Stage) (logincontroller.getMainnode().getScene().getWindow());
                                stage.close();
                            });
                        }
                    }
                }
            }
        }).start();
    }

    public static void setLogincontroller(LoginController logincontroller) {
        ReceiverHandler.logincontroller = logincontroller;
    }

    public static void setNotificationcontroller(NotificationController notificationcontroller) {
        ReceiverHandler.notificationcontroller = notificationcontroller;
    }
    
    public static void setFriendscontroller(FriendsController friendscontroller) {
        ReceiverHandler.friendscontroller = friendscontroller;
    }

    public static void setWishListcontroller(WishListController wishListcontroller) {
        ReceiverHandler.wishlistcontroller = wishListcontroller;
    }

    public static void setSignupcontroller(SignupController signupcontroller) {
        ReceiverHandler.signupcontroller = signupcontroller;
    }
    
    public static void setItemscontroller(ItemsController itemscontroller) {
        ReceiverHandler.itemscontroller = itemscontroller;
    }

    public static FriendsController getFriendscontroller() {
        return friendscontroller;
    }

    public static void setFriendprofilecontroller(FriendProfileController friendprofilecontroller) {
        ReceiverHandler.friendprofilecontroller = friendprofilecontroller;
    }

    public static void setPaymentcontroller(PaymentController paymentcontroller) {
        ReceiverHandler.paymentcontroller = paymentcontroller;
    }
    
    public static FriendProfileController getFriendprofilecontroller() {
        return friendprofilecontroller;
    }

    public static WishListController getWishlistcontroller() {
        return wishlistcontroller;
    }

    public static HomeController getHomecontroller() {
        return homecontroller;
    }

    public static void setHomecontroller(HomeController homecontroller) {
        ReceiverHandler.homecontroller = homecontroller;
    }
    
}
