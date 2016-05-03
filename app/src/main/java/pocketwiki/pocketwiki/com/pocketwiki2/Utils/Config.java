package pocketwiki.pocketwiki.com.pocketwiki2.Utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chinmay on 4/4/16.
 */
public class Config {

    public static final int SPLASH_ACTIVITY_DELAY = 1000;
    public static final int DATA_TYPE_CITY = 0;
    public static final int DATA_TYPE_CATEGORY = 1;
    public static final int DATA_TYPE_IMAGE = 2;
    public static final int DATA_TYPE_AUDIO = 3;
    public static int OPERATION_MODE = -1;
    public static final int MODE_OFFLINE = 0;
    public static final int MODE_ONLINE = 1;
    public static final long DEFAULT_LANGUAGE_ID = 1;
    public static final String KEY_ACTIVITY_RECREATED = "activity_recreated";
    public static final String KEY_FOR_NEARBY_ENTITIES = "for_nearby_entities";
    public static final String LOCAL_DB_NAME = "PocketWiki-db";
    public static final String DOWNLOAD_FOLDER_NAME = "PocketWiki";

    public static final String KEY_AREA_ENTITY_ID = "area_entity_id";
    public static final String KEY_ENTITY_ID = "entity_id";
    public static final String KEY_SUB_ENTITY_ID = "sub_entity_id";
    public static final String KEY_AREA_ENTITY_OBJECT = "area_entity";
    public static final String KEY_CATEGORY_ENTITY_OBJECT = "entity_category";
    public static final String KEY_SUB_ENTITIES_ARRAY = "array_sub_entities";
    public static final String KEY_DATA = "data";
    public static final String KEY_CITIES = "cities";
    public static final String KEY_CATEGORIES = "categories";
    public static final String KEY_ENTITIES = "entities";
    public static final String KEY_SUB_ENTITIES = "sub_entities";
    public static final String KEY_AREAS = "areas";
    public static final String KEY_CITY_NAME = "city_name";
    public static final String KEY_AREA_NAME = "area_name";
    public static final String KEY_AREA = "area";
    public static final String KEY_CITY = "city";
    public static final String KEY_SUB_ENTITY_NAME = "name";
    public static final String KEY_ENTITY_NAME = "entity";
    public static final String KEY_CATEGORY_NAME = "category_name";
    public static final String KEY_ID = "id";
    public static final String KEY_CITY_ID = "city_id";
    public static final String KEY_AREA_ID = "area_id";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_ENTITY_COUNT = "entity_count";
    public static final String KEY_MODIFIED_BY = "modified_by";
    public static final String KEY_UPDATED_AT = "updated_at";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CODE = "code";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_ERRORS = "errors";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_AUDIO = "audio";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_IMAGES_THUMB = "thumb";
    public static final String KEY_IMAGES_LARGE = "large";

    public static ArrayList<Long> CityIDHolder = new ArrayList<>();
    public static ArrayList<Long> CategoryIDHolder = new ArrayList<>();
    public static ArrayList<Long> AreaIDHolder = new ArrayList<>();

    public static final String URL_ROOT = "http://pocketwiki-api.herokuapp.com";
    public static final String URL_VERSION = "v1";
    public static final String URL_GET_CITIES = URL_VERSION + "/cities";
    public static final String URL_GET_CATEGORIES = URL_VERSION + "/categories";
    public static String URL_GET_AREAS(List<Long> categoryIdList, List<Long> cityIdList){
        Log.i("config",categoryIdList.toString() + " , " + cityIdList.toString());

        String retStr = URL_VERSION;
        int i;
        retStr = retStr + "/areas?category_ids=";
        for(i=0; i<categoryIdList.size()-1; i++){
            retStr = retStr + categoryIdList.get(i).toString() + ",";
        }
        if(!categoryIdList.isEmpty())
        retStr = retStr + categoryIdList.get(i).toString();

        retStr = retStr + "&city_ids=";
        for(i=0; i<cityIdList.size()-1; i++){
            retStr = retStr + cityIdList.get(i).toString() + ",";
        }
        if(!cityIdList.isEmpty())
        retStr = retStr + cityIdList.get(i).toString();

        Log.i("config",retStr);
        return retStr;
    }
    public static String URL_GET_ENTITIES(List<Long> areaIdList, List<Long> categoryIdList){
        Log.i("config",areaIdList.toString() + " , " + categoryIdList.toString());

        String retStr = URL_VERSION;
        int i;
        retStr = retStr + "/entities?area_ids=";
        for(i=0; i<areaIdList.size()-1; i++){
            retStr = retStr + areaIdList.get(i).toString() + ",";
        }
        if(!areaIdList.isEmpty())
            retStr = retStr + areaIdList.get(i).toString();

        retStr = retStr + "&category_ids=";
        for(i=0; i<categoryIdList.size()-1; i++){
            retStr = retStr + categoryIdList.get(i).toString() + ",";
        }
        if(!categoryIdList.isEmpty())
            retStr = retStr + categoryIdList.get(i).toString();

        Log.i("config",retStr);
        return retStr;
    }
    public static String URL_GET_SUB_ENTITIES(Long entityId){

        String retStr = URL_VERSION;
        retStr = retStr + "/entities/" + String.valueOf(entityId) + "/sub_entities";
        Log.i("config",retStr);
        return retStr;

    }
    public static String URL_GET_CONTENT(Long languageId, Long entityId, Long subEntityId){
        String retStr = URL_VERSION;
        retStr = retStr + "/contents?";
        retStr = retStr + "language_id=" + String.valueOf(languageId);
        retStr = retStr + "&entity_id=" + String.valueOf(entityId);
        retStr = retStr + "&sub_entity_id=" + String.valueOf(subEntityId);
        Log.i("config",retStr);
        return retStr;
    }
    public static String URL_GET_CONTENT(Long languageId, Long entityId){
        String retStr = URL_VERSION;
        retStr = retStr + "/contents?";
        retStr = retStr + "language_id=" + String.valueOf(languageId);
        retStr = retStr + "&entity_id=" + String.valueOf(entityId);
        Log.i("config",retStr);
        return retStr;
    }
    public static String URL_GET_NEARBY_ENTITIES(String area){

        String retStr = URL_VERSION;
        retStr = retStr + "/entities/search?query=";
        retStr = retStr.concat(area);
        retStr = retStr.replace(" ","%20");
        Log.i("config",retStr);
        return retStr;
    }
}
