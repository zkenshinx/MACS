/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {

	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		GLabel messageLabel = new GLabel(msg);
		messageLabel.setFont(MESSAGE_FONT);
		add(messageLabel, this.getWidth()/2 - messageLabel.getWidth()/2, getHeight()-BOTTOM_MESSAGE_MARGIN);
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		if (profile != null) {
			// Show Name Above The Picture
			String name = profile.getName();
			GLabel nameLabel = new GLabel(name);
			nameLabel.setColor(Color.BLUE);
			nameLabel.setFont(PROFILE_NAME_FONT);
			add(nameLabel, LEFT_MARGIN, TOP_MARGIN + nameLabel.getHeight());
			
			// Show Picture
			if (profile.getImage() == null) {
				GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
				add(rect, LEFT_MARGIN, nameLabel.getY() + IMAGE_MARGIN);
				GLabel label = new GLabel("No Image");
				label.setFont(PROFILE_IMAGE_FONT);
				add(label, rect.getX() + rect.getWidth()/2 - label.getWidth()/2, 
						rect.getY() + rect.getHeight()/2 + nameLabel.getAscent()/2);
			} else {
				GImage picture = profile.getImage();
				picture.scale(IMAGE_WIDTH / picture.getWidth(), IMAGE_HEIGHT / picture.getHeight());
				add(picture, LEFT_MARGIN, nameLabel.getY() + IMAGE_MARGIN);
			}
			
			// Show Status
			GLabel status = new GLabel("No current status");
			if (!profile.getStatus().equals("")) {
				status.setLabel(profile.getStatus());
			} 
			status.setFont(PROFILE_STATUS_FONT);
			add(status, LEFT_MARGIN, nameLabel.getY() + IMAGE_MARGIN + IMAGE_HEIGHT + status.getHeight());
			
			// Show Friend List
			GLabel label = new GLabel("Friends:");
			label.setFont(PROFILE_FRIEND_LABEL_FONT);
			add(label, getWidth()/2 - label.getWidth()/2, nameLabel.getY() + IMAGE_MARGIN);
			
			double friendLabelX = label.getX();
			double nextFriendLabelY = label.getY();
			Iterator<String> friendsIterator = profile.getFriends();
			while (friendsIterator.hasNext()) {
				GLabel friendLabel = new GLabel(friendsIterator.next());
				friendLabel.setFont(PROFILE_FRIEND_FONT);
				nextFriendLabelY += friendLabel.getHeight();
				add(friendLabel, friendLabelX, nextFriendLabelY);
			}
		}
	}	
}
