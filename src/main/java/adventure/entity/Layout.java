package adventure.entity;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import br.gov.frameworkdemoiselle.util.Reflections;

@Embeddable
public class Layout {

	@Column(name = "text_color")
	private String textColor;

	@Column(name = "background_color")
	private String backgroundColor;

	@Column(name = "button_color")
	private String buttonColor;

	public boolean isEmpty() {
		boolean resut = true;

		for (Field field : Reflections.getNonStaticDeclaredFields(this.getClass())) {
			resut &= Reflections.getFieldValue(field, this) == null;
		}

		return resut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((backgroundColor == null) ? 0 : backgroundColor.hashCode());
		result = prime * result + ((buttonColor == null) ? 0 : buttonColor.hashCode());
		result = prime * result + ((textColor == null) ? 0 : textColor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Layout)) {
			return false;
		}
		Layout other = (Layout) obj;
		if (backgroundColor == null) {
			if (other.backgroundColor != null) {
				return false;
			}
		} else if (!backgroundColor.equals(other.backgroundColor)) {
			return false;
		}
		if (buttonColor == null) {
			if (other.buttonColor != null) {
				return false;
			}
		} else if (!buttonColor.equals(other.buttonColor)) {
			return false;
		}
		if (textColor == null) {
			if (other.textColor != null) {
				return false;
			}
		} else if (!textColor.equals(other.textColor)) {
			return false;
		}
		return true;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getButtonColor() {
		return buttonColor;
	}

	public void setButtonColor(String buttonColor) {
		this.buttonColor = buttonColor;
	}
}
