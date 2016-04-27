package pocketwiki.pocketwiki.com.pocketwiki2;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class PocketWikiDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "pocketwiki.pocketwiki.com.pocketwiki2.Dao");

        Entity city = schema.addEntity("City");
        Property cityId = city.addLongProperty("cityId").primaryKey().getProperty();
        city.addStringProperty("createdAt");
        city.addStringProperty("updatedAt");
        city.addStringProperty("name");
        city.addIntProperty("entityCount");

        Entity area = schema.addEntity("Area");
        Property areaId = area.addLongProperty("areaId").primaryKey().getProperty();
        area.addStringProperty("createdAt");
        area.addStringProperty("updatedAt");
        area.addStringProperty("name");
        area.addStringProperty("cityName");
        Property fkCityId = area.addLongProperty("cityId").getProperty();
        area.addToOne(city,fkCityId);

        Entity subEntity = schema.addEntity("SubEntity");
        Property subEntityId = subEntity.addLongProperty("subEntityId").primaryKey().getProperty();
        subEntity.addStringProperty("createdAt");
        subEntity.addStringProperty("updatedAt");
        subEntity.addStringProperty("name");
        Property fkEntityId1 = subEntity.addLongProperty("entityId").getProperty();
        subEntity.addStringProperty("imageURLThumb");
        subEntity.addStringProperty("imageURLLarge");
        subEntity.addStringProperty("imageURLThumbOnline");
        subEntity.addStringProperty("imageURLLargeOnline");

        Entity entity = schema.addEntity("Entity");
        Property entityId = entity.addLongProperty("entityId").primaryKey().getProperty();
        entity.addStringProperty("createdAt");
        entity.addStringProperty("updatedAt");
        entity.addStringProperty("name");
        entity.addStringProperty("subEntityArray");
        entity.addToMany(subEntity,fkEntityId1);
        entity.addStringProperty("imageURLThumb");
        entity.addStringProperty("imageURLLarge");
        entity.addStringProperty("imageURLThumbOnline");
        entity.addStringProperty("imageURLLargeOnline");

        Entity category = schema.addEntity("Category");
        Property categoryId = category.addLongProperty("categoryId").primaryKey().getProperty();
        category.addStringProperty("createdAt");
        category.addStringProperty("updatedAt");
        category.addStringProperty("name");
        category.addIntProperty("entityCount");

        Entity language = schema.addEntity("Language");
        Property languageId = language.addLongProperty("languageId").primaryKey().getProperty();
        language.addStringProperty("createdAt");
        language.addStringProperty("updatedAt");
        language.addStringProperty("name");
        language.addBooleanProperty("defaultLanguageFlag");

        Entity areaEntities = schema.addEntity("AreaEntities");
        Property areaEntitiesId = areaEntities.addLongProperty("areaEntitiesId").primaryKey().getProperty();
        areaEntities.addStringProperty("createdAt");
        areaEntities.addStringProperty("updatedAt");
        Property fkAreaId = areaEntities.addLongProperty("areaId").getProperty();
        Property fkEntityId = areaEntities.addLongProperty("entityId").getProperty();
        area.addToMany(areaEntities,fkAreaId);
        entity.addToMany(areaEntities,fkEntityId);

        Entity categoryEntities = schema.addEntity("CategoryEntities");
        Property categoryEntitiesId = categoryEntities.addLongProperty("categoryEntitiesId").primaryKey().getProperty();
        categoryEntities.addStringProperty("createdAt");
        categoryEntities.addStringProperty("updatedAt");
        Property fkCategoryId = categoryEntities.addLongProperty("categoryId").getProperty();
        Property fkEntityId2 = categoryEntities.addLongProperty("entityId").getProperty();
        category.addToMany(categoryEntities,fkCategoryId);
        entity.addToMany(categoryEntities,fkEntityId2);

        Entity content = schema.addEntity("Content");
        Property contentId = content.addLongProperty("contentId").primaryKey().getProperty();
        content.addStringProperty("createdAt");
        content.addStringProperty("updatedAt");
        content.addStringProperty("description");
        content.addLongProperty("subEntityId");
        content.addStringProperty("audioPath");
        Property fkLanguageId = content.addLongProperty("languageId").getProperty();
        Property fkAreaEntitiesId = content.addLongProperty("areaEntitiesId").getProperty();
        content.addToOne(language,fkLanguageId);
        content.addToOne(areaEntities,fkAreaEntitiesId);

        new DaoGenerator().generateAll(schema, "../app/src/main/java");

    }
}
