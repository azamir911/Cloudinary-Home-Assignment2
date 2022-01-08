package com.cloudinary.service;

import com.cloudinary.image.ImageDimensions;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

/*
 * Responsible for handling the image
 */
public class ImageService {

	private static ImageService instance = null;
	
	public static ImageService getInstance() {
		if (instance == null) {
			instance = new ImageService();
		}
		
		return instance;
	}
	
	private ImageService() {
		
	}
	
	// Calculating the new dimensions based on the parameters
	public ImageDimensions getNewDimensions(int baseWidth, int baseHeight, int destinationWidth, int destinationHeight) {
		ImageDimensions imageDimensions = new ImageDimensions(baseWidth, baseHeight);
		
		imageDimensions.calculateDimension(destinationWidth, destinationHeight);
		
		return imageDimensions;
	}
	
	// Create a new BufferedImage and return it with the new size
	public BufferedImage resizeImage(BufferedImage originalImage, ImageDimensions imageDimensions) {
	    BufferedImage resizedImage = new BufferedImage(imageDimensions.getWidth(), imageDimensions.getHeight(), 1);
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, imageDimensions.getWidth(), imageDimensions.getHeight(), null);
	    g.dispose();

	    return resizedImage;
	}

	// Return the BufferedImage as a binary string
	public String getAsBinary(BufferedImage scaledInstance) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(scaledInstance, "jpg", baos);
		
		baos.flush();

		return DatatypeConverter.printBase64Binary(baos.toByteArray());
	}
	
	// Reading the image from the URL
	public BufferedImage readImage(URL url) throws IOException {
		return ImageIO.read(url);
	}
	
}
