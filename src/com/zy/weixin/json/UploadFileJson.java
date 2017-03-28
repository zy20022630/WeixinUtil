package com.zy.weixin.json;

/**
 * 上传多媒体文件成功的JSON<br/>
 * <br/>
 * 当type=image时，type、media_id和created_at有值；<br/>
 * 当type=voice时，type、media_id和created_at有值；<br/>
 * 当type=video时，type、media_id、thumb_media_id和created_at有值；<br/>
 * 当type=thumb时，type、thumb_media_id和created_at有值
 * @author zy20022630
 */
public class UploadFileJson {
	
	private String type;			//媒体文件类型，分别有图片(image)、语音(voice)、视频(video)和缩略图(thumb，主要用于视频与音乐格式的缩略图)
	private String media_id;		//媒体文件上传后，获取时的唯一标识(即多媒体文件ID)
	private String thumb_media_id;	//缩略图多媒体文件ID
	private long created_at;		//媒体文件上传时间戳
	
	public UploadFileJson() {
		super();
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}
}