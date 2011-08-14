package com.giago.www.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class CanvasEntryPoint implements EntryPoint {

	public static final CssColor REDRAW_COLOR = CssColor.make("rgba(255,255,255,0.6)");

	private static final String HOOK = "gwt-hook";
	private static final String PIXEL = "px";
	private static final int REFRESH_RATE = 35; //25 ms
	private static final int HEIGHT = 161;
	private static final int WIDTH = 396;
	private static final String WIDTH_PX = WIDTH + PIXEL;
	private static final String HEIGHT_PX = HEIGHT + PIXEL;
	
	private Canvas canvas;
	private Context2d context;
	
	private Logo logo;
	
	private boolean animate = false;
	
	@Override
	public void onModuleLoad() {
		initCanvas();		
		logo = new Logo(WIDTH, HEIGHT) {
			@Override
			protected void onImageLoaded() {
				doUpdate();
			}
		};
		initHandlers();
		setupTimer();
	}
	
	private void setupTimer() {
		final Timer timer = new Timer() {
			@Override
			public void run() {
				if(animate) {
					doUpdate();
				}
			}
		};
		timer.scheduleRepeating(REFRESH_RATE);
	}
	
	private void initCanvas() {
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			RootPanel.get(HOOK).add(
					new Label("browser doesn't support HTML5, get Chrome!!!"));
			return;
		}
		canvas.setWidth(WIDTH_PX);
		canvas.setHeight(HEIGHT_PX);
		canvas.setCoordinateSpaceWidth(WIDTH);
		canvas.setCoordinateSpaceHeight(HEIGHT);
		RootPanel.get(HOOK).add(canvas);
		
		context = canvas.getContext2d();
		context.setFillStyle(REDRAW_COLOR);
		context.fillRect(0, 0, WIDTH, HEIGHT);
	}

	private void doUpdate() {		
		logo.update();
		logo.draw(context);
	}

	private void initHandlers() {
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				animate = true;
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				animate = false;
			}
		});
		canvas.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.open("/about.jsp", "_self", null);
			}			
		});
	}
}
