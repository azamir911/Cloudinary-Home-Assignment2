package com.cloudinary.image;

public class ImageDimensions {
	
	private int width;
	private int height;
	
	private int topBottomPadding;
	private int leftRightPadding;
	
	public ImageDimensions(int baseWidth, int baseHeight) {
		this.width = baseWidth;
		this.height = baseHeight;
	}
	
	public void calculateDimension(int destinationWidth, int destinationHeight) {
		int sourceWidth = this.width;
		int sourceHeight = this.height;

	    // first check if we need to scale width
	    if (sourceWidth > destinationWidth) {
	        //scale width to fit
	        width = destinationWidth;
	        //scale height to maintain aspect ratio
	        height = (width * sourceHeight) / sourceWidth;
	        
	        if (((width * sourceHeight) % sourceWidth) > 0) {
	        	height++;
	        }
	    }

	    // then check if we need to scale even with the new height
	    if (height > destinationHeight) {
	        //scale height to fit instead
	        height = destinationHeight;
	        
	        topBottomPadding = 0;
	        //scale width to maintain aspect ratio
	        width = (height * sourceWidth) / sourceHeight;
	        
	        if (((height * sourceWidth) % sourceHeight) > 0) {
	        	width++;
	        }
	    }

	    topBottomPadding = (destinationHeight - height) / 2;
	    leftRightPadding = (destinationWidth - width) / 2;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTopBottomPadding() {
		return topBottomPadding;
	}

	public int getLeftRightPadding() {
		return leftRightPadding;
	}

}
