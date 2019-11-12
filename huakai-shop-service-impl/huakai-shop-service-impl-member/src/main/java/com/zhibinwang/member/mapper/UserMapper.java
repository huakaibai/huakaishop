package com.zhibinwang.member.mapper;

import com.zhibinwang.member.entity.UserDo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 花开
 */
public interface UserMapper {

	/**
	 * 注册用户
	 * @param userDo
	 * @return int
	 */
	@Insert("INSERT INTO `meite_user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, null, null, null, '1', null, null, null);")
	int register(UserDo userDo);

	/**判断用户是否存在
	 *
	 * @param mobile
	 * @return UserDo
	 */
	@Select("SELECT * FROM meite_user WHERE MOBILE=#{mobile};")
	UserDo existMobile(@Param("mobile") String mobile);

	@Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID "
			+ "  FROM meite_user  WHERE MOBILE=#{0} and password=#{1};")
	UserDo login(@Param("mobile") String mobile, @Param("password") String password);

	@Select("SELECT USER_ID AS USERID ,MOBILE AS MOBILE,EMAIL AS EMAIL,PASSWORD AS PASSWORD, USER_NAME AS USER_NAME ,SEX AS SEX ,AGE AS AGE ,CREATE_TIME AS CREATETIME,IS_AVALIBLE AS ISAVALIBLE,PIC_IMG AS PICIMG,QQ_OPENID AS QQOPENID,WX_OPENID AS WXOPENID"
			+ " FROM meite_user WHERE user_Id=#{userId}")
	UserDo findByUserId(@Param("userId") Long userId);





}
