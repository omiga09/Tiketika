package com.tiketika.Tiketika.common.helper;

import com.tiketika.Tiketika.common.images.enums.ImageType;

public class ImageCategoryMapper {

    public static String categoryFor(ImageType imageType) {
        switch (imageType) {
            case PROFILE:
            case NIDA_FRONT:
            case NIDA_BACK:
            case LOGO:
            case PRODUCT_IMAGE:
                return "images";
            case LICENSE:
            case TIN_CERT:
            case VRN_DOC:
            case CONTRACT:
                return "contracts";
            case KEY:
                return "keys";
            default:
                throw new IllegalArgumentException("Unknown image type: " + imageType);
        }
    }
}
