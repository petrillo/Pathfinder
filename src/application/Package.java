package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import application.Config.Tools;
import application.Config.UserAction;

public class Package {

	private static final double resizeArea = 15.0;

	@FXML VBox thePackage = new VBox();
	@FXML Pane packageInwards = new Pane();
	@FXML TextField packageNameEditor = new TextField();
	private Tooltip packageNameTooltip = new Tooltip();

	double x = 0;
	double y = 0;
	double dragX = 0;
	double dragY = 0;

	@FXML
	public void initialize() {
		this.packageNameTooltip.textProperty().bind(this.packageNameEditor.textProperty());
		Tooltip.install(this.packageInwards, this.packageNameTooltip);
		Tooltip.install(this.thePackage, this.packageNameTooltip);
	}
	
	@FXML
	protected void mouseClicked(MouseEvent event) {
		
		switch (Config.getCurrentTool()) {
			case SELECTION:
				if (event.getButton() == MouseButton.PRIMARY) {
					if (event.getClickCount() == 2) {
						this.packageNameEditor.toFront();
						this.packageNameEditor.setVisible(true);
						this.packageNameEditor.requestFocus();
					}
				}
				Config.setCurrentTool(Tools.SELECTION);
				break;
			case PACKAGE:
				if (event.getButton() == MouseButton.PRIMARY) {
					try {
						Parent newPackage = FXMLLoader.load(getClass().getResource("Package.fxml"));
						if (this.packageInwards.getWidth() < this.thePackage.getMinWidth()) {
							this.thePackage.setPrefWidth(thePackage.getMinWidth()*2 - this.packageInwards.getWidth());
						}
						if (this.packageInwards.getHeight() < this.thePackage.getMinHeight()) {
							this.thePackage.setPrefHeight(thePackage.getMinHeight()*2 - this.packageInwards.getHeight());
						}
						this.packageInwards.getChildren().add(newPackage);
						Config.setCurrentTool(Tools.SELECTION);
					} catch (IOException e) {
						// TODO : Exception handler
					}
				}
				break;
			case CLASS:
				if (event.getButton() == MouseButton.PRIMARY) {
					try {
						Parent newClass = FXMLLoader.load(getClass().getResource("Class.fxml"));
						if (this.packageInwards.getWidth() < this.thePackage.getMinWidth()) {
							this.thePackage.setPrefWidth(thePackage.getMinWidth()*2 - this.packageInwards.getWidth());
						}
						if (this.packageInwards.getHeight() < this.thePackage.getMinHeight()) {
							this.thePackage.setPrefHeight(thePackage.getMinHeight()*2 - this.packageInwards.getHeight());
						}
						this.packageInwards.getChildren().add(newClass);
						Config.setCurrentTool(Tools.SELECTION);
					} catch (IOException e) {
						// TODO : Exception handler
					}
				}
				break;
			case ERASE:
				if (event.getButton() == MouseButton.PRIMARY) {
					((Pane) this.thePackage.getParent()).getChildren().remove(this.thePackage);
					Config.setCurrentTool(Tools.SELECTION);
				}
				break;
			default:
				break;
		}
		event.consume();
	}

	@FXML
	protected void mouseDragged(MouseEvent event) {
		switch (Config.getUserAction()) {
			case NOTHING:
				double newX = this.x + event.getSceneX() - this.dragX;
				if (newX >= 0) {
					if (newX+this.thePackage.getWidth() <= ((Pane) this.thePackage.getParent()).getWidth()) {
						this.thePackage.setLayoutX(newX);
					} else {
						this.thePackage.setLayoutX(((Pane) this.thePackage.getParent()).getWidth()-this.thePackage.getWidth());
					}
				} else {
					this.thePackage.setLayoutX(0.0);
				}
				
				double newY = this.y + event.getSceneY() - this.dragY;
				if (newY >= 0) {
					if (newY+this.thePackage.getHeight() <= ((Pane) this.thePackage.getParent()).getHeight()) {
						this.thePackage.setLayoutY(newY);
					} else {
						this.thePackage.setLayoutY(((Pane) this.thePackage.getParent()).getHeight()-this.thePackage.getHeight());
					}
				} else {
					this.thePackage.setLayoutY(0.0);
				}
				break;
			case RESIZING:
				double newWidth = this.x + event.getSceneX() - this.dragX;
				if (newWidth >= this.thePackage.getMinWidth()) {
					if (newWidth <= ((Pane) this.thePackage.getParent()).getWidth() - this.thePackage.getLayoutX()) {
						this.thePackage.setPrefWidth(newWidth);
					} else {
						this.thePackage.setPrefWidth(((Pane) this.thePackage.getParent()).getWidth() - this.thePackage.getLayoutX());
					}
				} else {
					this.thePackage.setPrefWidth(this.thePackage.getMinWidth());
				}
				double newHeight = this.y + event.getSceneY() - this.dragY;
				if (newHeight >= this.thePackage.getMinHeight()) {
					if (newHeight <= ((Pane) this.thePackage.getParent()).getHeight() - this.thePackage.getLayoutY()) {
						this.thePackage.setPrefHeight(newHeight);
					} else {
						this.thePackage.setPrefHeight(((Pane) this.thePackage.getParent()).getHeight() - this.thePackage.getLayoutY());
					}
				} else {
					this.thePackage.setPrefHeight(this.thePackage.getMinHeight());
				}
				break;
			default:
				break;
		}
		event.consume();
	}

	@FXML
	protected void mouseMoved(MouseEvent event) {
		if ((event.getX() > this.thePackage.getTranslateX() + this.thePackage.getPrefWidth() - resizeArea) && (event.getY() > this.thePackage.getTranslateY() + this.thePackage.getPrefHeight() - resizeArea)) {
			this.thePackage.setCursor(Cursor.SE_RESIZE);
		} else {
			this.thePackage.setCursor(Cursor.DEFAULT);
		}
		event.consume();
	}

	@FXML
	protected void mousePressed(MouseEvent event) {
		this.dragX = event.getSceneX();
		this.dragY = event.getSceneY();
		if ((event.getX() > this.thePackage.getTranslateX() + this.thePackage.getPrefWidth() - resizeArea) && (event.getY() > this.thePackage.getTranslateY() + this.thePackage.getPrefHeight() - resizeArea)) {
			Config.setUserAction(UserAction.RESIZING);
			this.x = this.thePackage.getWidth();
			this.y = this.thePackage.getHeight();
		} else {
			this.x = this.thePackage.getLayoutX();
			this.y = this.thePackage.getLayoutY();
		}
		event.consume();
	}

	@FXML
	protected void mouseReleased(MouseEvent event) {
		Config.setUserAction(UserAction.NOTHING);
		event.consume();
	}

	@FXML
	public void packageNameEditorAction(ActionEvent event) {
		this.packageNameEditor.setVisible(false);
		// TODO: Search why these things doesn't work on contructor. ��
		if (!this.packageNameTooltip.textProperty().isBound()) {
			this.packageNameTooltip.textProperty().bind(this.packageNameEditor.textProperty());
		}
	}
}
