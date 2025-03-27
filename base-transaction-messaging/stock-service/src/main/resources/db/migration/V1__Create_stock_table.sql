CREATE TABLE "stock" (
                         "id" INT AUTO_INCREMENT,
                         "item_name" varchar(50),
                         "total_qty" decimal(10,2),
                         "locked_qty" decimal(10,2),
                         PRIMARY KEY ("id")
)
;


INSERT INTO "stock" ("item_name", "total_qty", "locked_qty") VALUES ('item1', 100, 0.0);
INSERT INTO "stock" ("item_name", "total_qty", "locked_qty") VALUES ('item2', 50.3, 0.0);
INSERT INTO "stock" ("item_name", "total_qty", "locked_qty") VALUES ('item3', 10.7, 0.0);