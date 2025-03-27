CREATE TABLE "order" (
                         "id" int NOT NULL AUTO_INCREMENT,
                         "code" varchar(50) NOT NULL,
                         "item_name" varchar(50),
                         "qty" decimal(10,2) NOT NULL,
                         "create_time" date,
                         "update_time" date,
                         "status" int2,
                         PRIMARY KEY ("id")
)
;
CREATE INDEX "code" ON "order"USING btree (
    "code"
    );