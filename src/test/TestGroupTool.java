package test;

import java.util.ArrayList;
import java.util.List;

import com.zy.weixin.common.AccessTokenHelper;
import com.zy.weixin.common.WeiXinException;
import com.zy.weixin.json.CreateGroupJson;
import com.zy.weixin.json.GroupInfoJson;
import com.zy.weixin.json.GroupListJson;
import com.zy.weixin.tool.GroupTool;

public class TestGroupTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		test1();
//		test2();
		//test3();
//		test4();
//		test5();
//		test6();
	}

	static void test1(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;

		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			String groupName = "测试群组";
			CreateGroupJson createGroupJson = GroupTool.getInstance().createGroup(accessToken, groupName);
			if (createGroupJson == null)
				System.out.println("createGroupJson = null");
			else if (createGroupJson.getGroup() == null)
				System.out.println("createGroupJson.getGroup() = null");
			else {
				GroupInfoJson group = createGroupJson.getGroup();
				System.out.print(group.getId() + "\t");
				System.out.print(group.getName() + "\t");
				System.out.print(group.getCount() + "\t");
				System.out.println();
			}
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static void test2(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			GroupListJson groupListJson = GroupTool.getInstance().getGroupList(accessToken);
			if (groupListJson == null)
				System.out.println("groupListJson = null");
			else {
				List<GroupInfoJson> groups = groupListJson.getGroups();
				if (groups == null)
					System.out.println("groups = null");
				else if (groups.isEmpty())
					System.out.println("groups.size = 0");
				else {
					for (GroupInfoJson groupInfoJson : groups){
						System.out.print(groupInfoJson.getId() + "\t");
						System.out.print(groupInfoJson.getName() + "\t");
						System.out.print(groupInfoJson.getCount() + "\t");
						System.out.println();
					}
				}
			}
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static void test3(){
//		AccessTokenHelper helper = null;
//		String appid = null;
//		String appsecret = null;
		String accessToken = null;
		
//		appid = "xxxxxxxxxxxxxxxxx";
//		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	
		try {
//			helper = AccessTokenHelper.getInstance();
//			if (helper.getCredential(appid, appsecret))
//				accessToken = helper.getAccessToken();
			
			String openId = "ohOO1uNLRuUu2MnOy5ChK-ymPbfo";
			accessToken = "vB0PAYcJoBFvcSTkFiyMO9WR8u60vKZ6G9ZArfdjYFGWQqfGBOgPh5uon4CkZ2cKYX_8x6pTJy2LHG6c3A0GBJfF838uBYkaGrUqn9rLDaI";
			
			String groupId = GroupTool.getInstance().getGroupId(accessToken, openId);
			
			System.out.println("groupId = " + groupId);
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static void test4(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			String groupId = "101";
			String groupName = "测试群组一";
			
			boolean isOK = GroupTool.getInstance().updateGroupName(accessToken, groupId, groupName);
			
			System.out.println("isOK = " + isOK);
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static void test5(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			String openId = "ohOO1uNLRuUu2MnOy5ChK-ymPbfo";
			String toGroupId = "101";
			
			boolean isOK = GroupTool.getInstance().moveUserToGroup(accessToken, openId, toGroupId);
			
			System.out.println("isOK = " + isOK);
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	static void test6(){
		AccessTokenHelper helper = null;
		String appid = null;
		String appsecret = null;
		String accessToken = null;
		
		appid = "xxxxxxxxxxxxxxxxx";
		appsecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		try {
			helper = AccessTokenHelper.getInstance();
			if (helper.getCredential(appid, appsecret))
				accessToken = helper.getAccessToken();
			
			List<String> openIdList = new ArrayList<String>();
			openIdList.add("ohOO1uGEOey0mXMqnNW5jJ7XgPpM");
			openIdList.add("ohOO1uH7WCmrLdVJXaRDzRblJ4SY");
			openIdList.add("ohOO1uPOondyrGBYxSQeYbQYoMOs");
			openIdList.add("ohOO1uH9cAdEQIcT5zAsXwezE7W4");
			String toGroupId = "101";
			
			boolean isOK = GroupTool.getInstance().batchMoveUserToGroup(accessToken, openIdList, toGroupId);
			
			System.out.println("isOK = " + isOK);
			
		} catch (WeiXinException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}