package adventure.util;

import javax.inject.Named;

@Named
public class Constants {

	public static final String EVENT_SLUG_PATTERN = "[\\w\\d_\\-/]+";

	public static final String RACE_SLUG_PATTERN = "[\\w\\d\\-_]+";

	public static final Integer USER_PHOTO_WIDTH = 250;

	public static final Integer USER_PHOTO_HEIGHT = USER_PHOTO_WIDTH;

	public static final Integer USER_THUMBNAIL_WIDTH = 30;

	public static final Integer USER_THUMBNAIL_HEIGHT = USER_THUMBNAIL_WIDTH;

	public static final Integer EVENT_BANNER_WIDTH = 750;

	public static final Integer EVENT_BANNER_HEIGHT = 350;

	public static final int NAME_SIZE = 50;

	public int getNameSize() {
		return NAME_SIZE;
	}

	public static final int ABBREVIATION_SIZE = 2;

	public int getAbbreviationSize() {
		return ABBREVIATION_SIZE;
	}

	public static final int ACRONYM_SIZE = 3;

	public int getAcronymSize() {
		return ACRONYM_SIZE;
	}

	public static final int SLUG_SIZE = 255;

	public int getSlugSize() {
		return SLUG_SIZE;
	}

	public static final int EMAIL_SIZE = 255;

	public int getEmailSize() {
		return EMAIL_SIZE;
	}

	public static final int RG_SIZE = 10;

	public int getRgSize() {
		return RG_SIZE;
	}

	public static final int CPF_SIZE = 11;

	public int getCpfSize() {
		return CPF_SIZE;
	}

	public static final int PASSWORD_SIZE = 50;

	public int getPasswordSize() {
		return PASSWORD_SIZE;
	}

	public static final int HASH_SIZE = 64;

	public int getHashSize() {
		return HASH_SIZE;
	}

	public static final int TELEPHONE_SIZE = 15;

	public int getTelephoneSize() {
		return TELEPHONE_SIZE;
	}

	public static final int TEXT_SIZE = 700;

	public int getTextSize() {
		return TEXT_SIZE;
	}

	public static final int GENERIC_ID_SIZE = 20;

	public int getGenericIdSize() {
		return GENERIC_ID_SIZE;
	}

	public static final int ENUM_SIZE = 20;

	public int getEnumSize() {
		return ENUM_SIZE;
	}

	public static final int SMALL_DESCRIPTION_SIZE = 100;

	public int getSmalDescriptionSize() {
		return SMALL_DESCRIPTION_SIZE;
	}
}
