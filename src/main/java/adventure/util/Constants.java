package adventure.util;

import javax.inject.Named;

@Named
public class Constants {

	public static final String EVENT_SLUG_PATTERN = "[a-zA-Z][\\w_\\-/]+";

	public static final String RACE_SLUG_PATTERN = "[a-zA-Z][\\w\\-_]+";

	public static final Integer USER_PHOTO_WIDTH = 250;

	public static final Integer USER_PHOTO_HEIGHT = USER_PHOTO_WIDTH;

	public static final Integer USER_THUMBNAIL_WIDTH = 30;

	public static final Integer USER_THUMBNAIL_HEIGHT = USER_THUMBNAIL_WIDTH;

	public static final Integer EVENT_BANNER_WIDTH = 750;

	public static final Integer EVENT_BANNER_HEIGHT = 350;

	public static final int NAME_SIZE = 50;
	public static final int ABBREVIATION_SIZE = 2;
	public static final int ACRONYM_SIZE = 3;
	public static final int SLUG_SIZE = 255;
	public static final int EMAIL_SIZE = 255;
	public static final int RG_SIZE = 10;
	public static final int CPF_SIZE = 11;
	public static final int PASSWORD_SIZE = 50;
	public static final int HASH_SIZE = 64;
	public static final int TELEPHONE_SIZE = 15;
	public static final int TEXT_SIZE = 700;
	public static final int GENERIC_ID_SIZE = 20;
	public static final int ENUM_SIZE = 20;
	public static final int SMALL_DESCRIPTION_SIZE = 100;

	public int getNameSize() {
		return NAME_SIZE;
	}

	public int getAbbreviationSize() {
		return ABBREVIATION_SIZE;
	}

	public int getAcronymSize() {
		return ACRONYM_SIZE;
	}

	public int getAliasSize() {
		return SLUG_SIZE;
	}

	public int getEmailSize() {
		return EMAIL_SIZE;
	}

	public int getRgSize() {
		return RG_SIZE;
	}

	public int getCpfSize() {
		return CPF_SIZE;
	}

	public int getPasswordSize() {
		return PASSWORD_SIZE;
	}

	public int getHashSize() {
		return HASH_SIZE;
	}

	public int getTelephoneSize() {
		return TELEPHONE_SIZE;
	}

	public int getTextSize() {
		return TEXT_SIZE;
	}

	public int getGenericIdSize() {
		return GENERIC_ID_SIZE;
	}

	public int getEnumSize() {
		return ENUM_SIZE;
	}

	public int getSmalDescriptionSize() {
		return SMALL_DESCRIPTION_SIZE;
	}
}
