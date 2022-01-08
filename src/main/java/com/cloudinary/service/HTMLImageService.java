package com.cloudinary.service;


import com.cloudinary.image.ImageDimensions;

/*
 * Responsible for building the image tag
 */
public class HTMLImageService {

	private static HTMLImageService instance = null;
	
	public static HTMLImageService getInstance() {
		if (instance == null) {
			instance = new HTMLImageService();
		}
		
		return instance;
	}
	
	private HTMLImageService() {
		
	}

	// Return the image tag with new paddings
	public String getHTMLImageTag(String imageBinary, ImageDimensions newDimensions) {
		StringBuilder builder = getBaseBuilder(imageBinary);
		builder.append(" style=\"padding:").append(newDimensions.getTopBottomPadding()).append("px ");
		builder.append(newDimensions.getLeftRightPadding()).append("px;background-color:black");
		closeBaseBuilder(builder);
		
		return builder.toString();
	}

	// Return the image tag with no paddings
	public String getHTMLImageTag(String imageBinary) {
		StringBuilder builder = getBaseBuilder(imageBinary);
		closeBaseBuilder(builder);
		
		return builder.toString();
	}
	
	private StringBuilder getBaseBuilder(String imageBinary) {
		StringBuilder builder = new StringBuilder();
		builder.append("<img src='");
		builder.append("data:image/jpg;base64,").append(imageBinary).append("'");
		return builder;
	}
	
	private void closeBaseBuilder(StringBuilder builder) {
		builder.append("\">");
	}
	
	
}
