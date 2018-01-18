package com.mob.linyl.api;

import com.mob.linyl.info.Add;
import com.mob.linyl.info.BaseInfo;
import com.mob.linyl.info.BuildingLists;
import com.mob.linyl.info.ComfirmOrderUrl;
import com.mob.linyl.info.CommitInfo;
import com.mob.linyl.info.Common;
import com.mob.linyl.info.Communitys;
import com.mob.linyl.info.GroupList;
import com.mob.linyl.info.Image;
import com.mob.linyl.info.Index;
import com.mob.linyl.info.Login;
import com.mob.linyl.info.MaterialList;
import com.mob.linyl.info.Material_V2;
import com.mob.linyl.info.MessageLists;
import com.mob.linyl.info.NoticeDetail;
import com.mob.linyl.info.NoticeLists;
import com.mob.linyl.info.NoticeRead;
import com.mob.linyl.info.PrintInfo;
import com.mob.linyl.info.RepairOrderDetail;
import com.mob.linyl.info.RepairOrderLists;
import com.mob.linyl.info.RoomLists;
import com.mob.linyl.info.Rooms;
import com.mob.linyl.info.Rooms_V2;
import com.mob.linyl.info.SMS;
import com.mob.linyl.info.UnitLists;
import com.mob.linyl.info.Version;

import org.json.JSONArray;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface KernerlService {

    @POST("lylapp/v1/user/sms?")
    Observable<BaseInfo<SMS>> getLoginCode(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("phone") String phone);

    @POST("lylapp/v1/user/login?")
    Observable<BaseInfo<Login>> getLoginData(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("code") String code
            , @Query("device_no") String device_no, @Query("device_type") String device_type, @Query("phone") String phone, @Query("version_no") String version_no);

    @POST("lylapp/v1/home/index?")
    Observable<BaseInfo<Index>> getIndexData(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token);

    @POST("lylapp/v1/msg/list?")
    Observable<BaseInfo<MessageLists>> getMessageList(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("last_id") String last_id, @Query("rows") String rows);

    @POST("lylapp/v1/msg/notice-list?")
    Observable<BaseInfo<NoticeLists>> getNoticeList(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("last_id") String last_id, @Query("rows") String rows);

    @POST("lylapp/v1/msg/set-notice-read?")
    Observable<BaseInfo<NoticeRead>> getNoticeRead(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token);

    @POST("lylapp/v1/msg/notice-view?")
    Observable<BaseInfo<NoticeDetail>> getNoticeDetail(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("notice_id") String notice_id);

    @POST("lylapp/v1/repair/common?")
    Observable<BaseInfo<Common>> getCommonData(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token);

    @POST("lylapp/v1/repair/rooms?")
    Observable<BaseInfo<List<Rooms>>> getRoomsData(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("community_id") String community_id);

    @POST("lylapp/v2/repair/rooms?")
    Observable<BaseInfo<Rooms_V2>> getRooms(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("community_id") String community_id);

    @POST("lylapp/v2/repair/add?")
    Observable<BaseInfo<Add>> submitRepairData(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("community_id") String community_id, @Query("repair_type") String repair_type, @Query("expired_repair_time") String expired_repair_time, @Query("expired_repair_type") String expired_repair_type, @Query("repair_content") String repair_content
            , @Query("room_ids") String room_ids, @Query("contact_mobile") String phone, @Query("imgs") String imgs);

    @Multipart
    @POST("lylapp/v1/repair/upload?")
    Observable<BaseInfo<Image>> getPhotoUrl(@Part("rand") RequestBody rand, @Part("secret") RequestBody secret, @Part("timestamp") RequestBody timestamp, @Part("sign") RequestBody sign, @Part("token") RequestBody token
            , @Part MultipartBody.Part file);
//    @FormUrlEncoded
//    @POST("lylapp/v1/repair/upload?")
//    Observable<BaseInfo<Image>> getPhotoUrl(@Field("rand") String rand, @Field("secret") String secret, @Field("timestamp") String timestamp, @Field("sign") String sign, @Field("token") String token
//            , @Field("pic") File img);


    @POST("lylapp/v1/repair/list?")
    Observable<BaseInfo<RepairOrderLists>> getRepairOrderData(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("last_id") String last_id, @Query("rows") String rows, @Query("status") String status);

    @POST("lylapp/v1/repair/view?")
    Observable<BaseInfo<RepairOrderDetail>> getRepairOrderDetailData(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("issue_id") String issue_id);

    @POST("lylapp/v1/repair/edit-status?")
    Observable<BaseInfo> changeStatus(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("issue_id") String issue_id, @Query("status") String status);

    @POST("lylapp/v1/repair/offline-pay?")
    Observable<BaseInfo> pay(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("issue_id") String issue_id);

    @POST("lylapp/v1/repair/material-list?")
    Observable<BaseInfo<List<MaterialList>>> getMaterialList(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("issue_id") String issue_id);

    @POST("lylapp/v2/repair/material-list?")
    Observable<BaseInfo<Material_V2>> getMaterialList_V2(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("issue_id") String issue_id);

    //1.4.0
    @POST("lylapp/v1/repair/confirm-bill?")
    Observable<BaseInfo<ComfirmOrderUrl>> submitMaterial(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("issue_id") String issue_id, @Query("other_charge") String other_charge, @Query("total_price") String total_price, @Query("materials_list") JSONArray materials_list);

    @POST("lylapp/v2/repair/confirm-bill?")
    Observable<BaseInfo<ComfirmOrderUrl>> submitMaterialV2(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token
            , @Query("issue_id") String issue_id, @Query("other_charge") String other_charge, @Query("total_price") String total_price, @Query("materials_list") JSONArray materials_list,@Query("need_add_record") String need_add_record,@Query("content") String content,@Query("need_pay") String need_pay,@Query("repair_imgs") String repair_imgs,@Query("status") String status);


    @POST("lylapp/v1/user/logout?")
    Observable<BaseInfo> loginOut(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token);

    @POST("lylapp/v1/home/version?")
    Observable<BaseInfo<Version>> getVersionCode(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("app_type") String app_type);

    @POST("lylapp/v1/water-room/communitys?")
    Observable<BaseInfo<Communitys>> getCommunitys(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token);

    @POST("lylapp/v1/water-room/group-list?")
    Observable<BaseInfo<GroupList>> getGroupList(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("community_id") String community_id);

    @POST("lylapp/v1/water-room/building-list?")
    Observable<BaseInfo<BuildingLists>> getBuildList(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("community_id") String community_id, @Query("group_name") String group_name);

    @POST("lylapp/v1/water-room/unit-list?")
    Observable<BaseInfo<UnitLists>> getUnitList(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("community_id") String community_id, @Query("group_name") String group_name, @Query("building_name") String building_name);

    @POST("lylapp/v1/water-room/room-list?")
    Observable<BaseInfo<RoomLists>> getRoomList(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("community_id") String community_id, @Query("group_name") String group_name, @Query("building_name") String building_name, @Query("unit_name") String unit_name, @Query("reading_status") String reading_status);

    @POST("lylapp/v1/water/commit?")
    Observable<BaseInfo<CommitInfo>> commit(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("note") String note, @Query("room_id") String room_id, @Query("status") String status, @Query("ton") String ton);

    @POST("lylapp/v1/water/print?")
    Observable<BaseInfo<PrintInfo>> getPrintInfo(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("community_id") String community_id, @Query("id") String id);


    @POST("lylapp/v2/repair/add-record?")
    Observable<BaseInfo> getAddRecord(@Query("rand") String rand, @Query("secret") String secret, @Query("timestamp") String timestamp, @Query("sign") String sign, @Query("token") String token, @Query("content") String content, @Query("issue_id") String issue_id, @Query("repair_imgs") String repair_imgs, @Query("status") String status, @Query("need_pay") String need_pay);


}
