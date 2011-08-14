package com.giago.www.client;

import java.util.logging.Logger;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class Logo {

	private static final Logger logger = Logger.getLogger("Logo");
	
	private int width;
	private int height;
	private Image logoImg1;
	private Image logoImg2;
	private ImageElement image1Element;
	private boolean image2Loaded;
	private ImageElement image2Element;

	public Logo(int width, int height) {
		this.width = width;
		this.height = height;
		
		logger.info("logo width : " + this.width + " height : " + this.height);
		
		initImage();
		RootPanel.get().add(logoImg1);
		RootPanel.get().add(logoImg2);
	}

	private void initImage() {
		logoImg1 = new Image("/images/dot.png");
		logoImg1.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent event) {
				logger.info("onLoad : " + event.getSource());
				image1Element = (ImageElement) logoImg1.getElement().cast();
				onImageLoaded();
			}
		});
		logoImg1.setVisible(false);
		
		logoImg2 = new Image("/images/logo_no_dot.png");
		logoImg2.addLoadHandler(new LoadHandler() {
			public void onLoad(LoadEvent event) {
				logger.info("onLoad : " + event.getSource());
				image2Loaded = true;
				image2Element = (ImageElement) logoImg2.getElement().cast();
				onImageLoaded();
			}

		});
		logoImg2.setVisible(false);
		
	}

	protected abstract void onImageLoaded();
	
	public boolean isDownDirection = true;
	
	public void update() {
		if (!image2Loaded) {
			return;
		}
		logger.info("update");
		if(y == 10) {
			isDownDirection = false;
		}
		if(y == 0){
			isDownDirection = true;
		}
		if(isDownDirection) {
			y++;
		} else {
			y--;
		}
	}
	
	private double y;
	private boolean first = true;

	public void draw(Context2d context) {
		if (!image2Loaded) {
			return;
		}
		logger.info("draw");
		if(first) {
			context.save();
			context.drawImage(image2Element, 0, 0);
			context.restore();
			first = false;
		}
		context.save();
		context.setFillStyle(CanvasEntryPoint.REDRAW_COLOR);
		context.fillRect(106, 0 + y-1, 38, 35);
		context.drawImage(image1Element, 106, y);
		context.restore();
	}

}
