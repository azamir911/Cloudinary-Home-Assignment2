package com.cloudinary.ws;

import com.cloudinary.image.ImageDimensions;
import com.cloudinary.service.CacheService;
import com.cloudinary.service.HTMLImageService;
import com.cloudinary.service.ImageService;
import com.cloudinary.service.URLService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
@RestController
@RequestMapping("/api")
public class ThumbnailRest {

	private static final CacheService theCacheService = CacheService.getInstance();
	private static final HTMLImageService theHTMLImageService = HTMLImageService.getInstance();
	private static final ImageService theImageService = ImageService.getInstance();
	private static final URLService theURLService = URLService.getInstance();

	@RequestMapping(value = "/thumbnail", method = RequestMethod.GET)
	public ResponseEntity<Object> thumbnail(@RequestParam("url") String url, @RequestParam("width") String width, @RequestParam("height") String height) throws IOException {
		
		if (url == null || url.trim().isEmpty()) {
			return new ResponseEntity<>("url attribute not found", HttpStatus.NOT_FOUND);
		}
		else if (width == null || width.trim().isEmpty()) {
			return new ResponseEntity<>("width attribute not found", HttpStatus.NOT_FOUND);
		}
		else if (height == null || height.trim().isEmpty()) {
			return new ResponseEntity<>("height attribute not found", HttpStatus.NOT_FOUND);
		}

		int theWidth;
		int theHeight;
		// Checking validation for the width parameter
		try {
			theWidth = Integer.parseInt(width);
		}
		catch (NumberFormatException e) {
			return new ResponseEntity<>("got a bad value for width", HttpStatus.PRECONDITION_FAILED);
		}

		// Checking validation for the height parameter 
		try {
			theHeight = Integer.parseInt(height);
		}
		catch (NumberFormatException e) {
			return new ResponseEntity<>("got a bad value for height", HttpStatus.PRECONDITION_FAILED);
		}
		
		URL theUrl = new URL(url);
		
		// Checking if we already got this image before
		BufferedImage bufferedImage = theCacheService.get(theUrl);
		
		// If the image was not exists, then checking the URL, the content type, and add the new image to the cache.
		if (bufferedImage == null) {
			// Checking the URL 
			HttpURLConnection httpURLConnection = theURLService.getHttpURLConnection(theUrl);
			boolean doesURLExist = theURLService.doesURLExist(httpURLConnection);
			if (!doesURLExist) {
				return new ResponseEntity<>("Url not found", HttpStatus.NOT_FOUND);

			}
			// Checking the URL's content
			String urlContentType = theURLService.getURLContentType(httpURLConnection);
			if (!urlContentType.startsWith("image")) {
				return new ResponseEntity<>("Url is not an image", HttpStatus.NOT_FOUND);
			}
			
			// Reading the image from the url
			bufferedImage = theImageService.readImage(theUrl);
			
			// If successes, add it to the cache (next time it will be faster)
			if (bufferedImage != null) {
				theCacheService.put(theUrl, bufferedImage);
			}
		}
		
		// If image exist, start to resize it.
		if (bufferedImage != null) {
			ImageDimensions newDimensions = null;
			BufferedImage scaledInstance;
			
			// Checking if the new dimension is different from the based
			if (bufferedImage.getWidth() != theWidth || bufferedImage.getHeight() != theHeight) {
				newDimensions = theImageService.getNewDimensions(bufferedImage.getWidth(), bufferedImage.getHeight(), theWidth, theHeight);
				
				scaledInstance = theImageService.resizeImage(bufferedImage, newDimensions);
			}
			else {
				scaledInstance = bufferedImage;
			}
			
			// Getting the image binary
			String imageBinary = theImageService.getAsBinary(scaledInstance);
			String htmlImageTag;
			
			// Getting the HTML tag
			if (newDimensions != null) {
				htmlImageTag = theHTMLImageService.getHTMLImageTag(imageBinary, newDimensions);
			}
			else {
				htmlImageTag = theHTMLImageService.getHTMLImageTag(imageBinary);
			}

			// Write the tag to the response
			return new ResponseEntity<>(htmlImageTag, HttpStatus.OK);
		}

		return new ResponseEntity<>("Image was not found in the URL", HttpStatus.NOT_FOUND);
	}


}
