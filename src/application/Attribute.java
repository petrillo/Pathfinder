package application;

import java.util.ArrayList;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import application.Config.Tools;

public class Attribute extends Rectangle {

	private ArrayList<AttributeAccess> attributeAccess = new ArrayList<AttributeAccess>();

	private Tooltip attributeNameTooltip = new Tooltip();
	private TextField attributeNameEditor = new TextField("new attribute");

	private DoubleProperty connectionX, connectionY;
	
	private double x = 0, y = 0;
	private double dragX = 0, dragY = 0;
	
	public Attribute() {
		this.setId("theAttribute");
		this.attributeNameTooltip.textProperty().bind(this.attributeNameEditor.textProperty());
		Tooltip.install(this, this.attributeNameTooltip);
		this.attributeNameEditor.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Attribute.this.attributeNameEditor.setVisible(false);
			}
			
		});
		this.attributeNameEditor.setVisible(false);
		this.attributeNameEditor.setMinWidth(30.0);
		
		this.setHeight(40.0);
		this.setWidth(40.0);
		this.setFill(Color.ORANGE);
		this.setStroke(Color.BLACK);
		this.setStrokeWidth(1.0);
		
		this.connectionX = new SimpleDoubleProperty(0.0);
		this.connectionX.bind(this.layoutXProperty().add(20.0));
		this.connectionY = new SimpleDoubleProperty(0.0);
		this.connectionY.bind(this.layoutYProperty().add(50.0));

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				switch (Config.getCurrentTool()) {
					case SELECTION:
						if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
							Attribute.this.attributeNameEditor.setVisible(true);
							Attribute.this.attributeNameEditor.requestFocus();
							event.consume();
						}
						break;
					default:
						break;
				}
			}
			
		});
		this.setOnMouseDragged(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				if (Config.getCurrentTool() == Tools.SELECTION) {
					double newX = Attribute.this.x + event.getSceneX() - Attribute.this.dragX;
					if (newX >= 0) {
						if (newX+Attribute.this.getWidth() <= ((Pane) Attribute.this.getParent()).getWidth()) {
							Attribute.this.setLayoutX(newX);
						} else {
							Attribute.this.setLayoutX(((Pane) Attribute.this.getParent()).getWidth()-Attribute.this.getWidth());
						}
					} else {
						Attribute.this.setLayoutX(0.0);
					}
					
					double newY = Attribute.this.y + event.getSceneY() - Attribute.this.dragY;
					if (newY >= 0) {
						if (newY+Attribute.this.getHeight() <= ((Pane) Attribute.this.getParent()).getHeight()) {
							Attribute.this.setLayoutY(newY);
						} else {
							Attribute.this.setLayoutY(((Pane) Attribute.this.getParent()).getHeight()-Attribute.this.getHeight());
						}
					} else {
						Attribute.this.setLayoutY(0.0);
					}
				}
				event.consume();
			}
			
		});
		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				Attribute.this.dragX = event.getSceneX();
				Attribute.this.dragY = event.getSceneY();
				Attribute.this.x = Attribute.this.getLayoutX();
				Attribute.this.y = Attribute.this.getLayoutY();
				event.consume();
			}
			
		});
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				
			}
			
		});
	}

	public void updateName(Pane parent) {
		parent.getChildren().add(this.attributeNameEditor);
		this.attributeNameEditor.layoutXProperty().bind(this.layoutXProperty());
		this.attributeNameEditor.layoutYProperty().bind(this.layoutYProperty().add(this.getHeight() + this.attributeNameEditor.getHeight()));
	}

	public NumberBinding getConnectionX() {
		Parent recursiveParent = this.getParent().getParent();
		NumberBinding returnBinding = this.connectionX.add(recursiveParent.layoutXProperty());
		while (recursiveParent.getParent() != null && !recursiveParent.getParent().getId().equals("platformGround")) {
			recursiveParent = recursiveParent.getParent();
			if (recursiveParent.getId().equals("theClass") || recursiveParent.getId().equals("thePackage")) {
				returnBinding = returnBinding.add(recursiveParent.layoutXProperty());
			}
		}
		return returnBinding;
	}

	public NumberBinding getConnectionY() {
		Parent recursiveParent = this.getParent().getParent();
		NumberBinding returnBinding = this.connectionY.add(recursiveParent.layoutYProperty());
		while (recursiveParent.getParent() != null && !recursiveParent.getParent().getId().equals("platformGround")) {
			recursiveParent = recursiveParent.getParent();
			if (recursiveParent.getId().equals("theClass")) {
				returnBinding = returnBinding.add(recursiveParent.layoutYProperty().add(28.0));
			} else if (recursiveParent.getId().equals("thePackage")) {
				returnBinding = returnBinding.add(recursiveParent.layoutYProperty());
			}
		}
		return returnBinding;
	}
	
	public ArrayList<AttributeAccess> getAttributeAccesses() {
		return this.attributeAccess;
	}
	
	public void addAttributeAccess(AttributeAccess access) {
		this.attributeAccess.add(access);
	}
	
	public void removeAttributeAccess(AttributeAccess access) {
		this.attributeAccess.remove(access);
	}
	
	public void clearAccess() {
		for (int index = this.attributeAccess.size()-1; index == 0; --index) {
			this.attributeAccess.get(index).delete();
		}
	}

	public String getName() {
		return this.attributeNameEditor.getText();
	}
	
	public void setName(String name) {
		this.attributeNameEditor.setText(name);
	}
}
