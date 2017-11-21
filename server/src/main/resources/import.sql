INSERT INTO iot.users (email, name, password) VALUES ('admin@e-yantra.org', 'Super Admin', '123456');
INSERT INTO iot.users (email, name, password) VALUES ('test@e-yantra.org', 'Test User', '123456');

INSERT INTO iot.units (description, photo, unit_name, parent_id) VALUES ('fdf 5', 'http://p0.static.bookstruck.in.s3.amazonaws.com/images/6d638803187b4d2988598252d2ad3c7c.jpg', 'Unit_Name', null);
INSERT INTO iot.units (description, photo, unit_name, parent_id) VALUES ('subunit', null, 'Subunit 1', 1);
INSERT INTO iot.units (description, photo, unit_name, parent_id) VALUES ('Bearer', null, 'Bearer', 1);

INSERT INTO iot.session (token, user_id) VALUES ('e046b896-40ed-4088-bdb7-e15360e5977b', 1);
INSERT INTO iot.session (token, user_id) VALUES ('c6c1529e-c35b-49aa-9232-3cf53350551f', 1);
INSERT INTO iot.session (token, user_id) VALUES ('6916c097-74a7-4f99-82bf-2bf8271550c9', 1);
INSERT INTO iot.session (token, user_id) VALUES ('046f6916-f831-4575-a203-f5aa24d666f9', 1);
INSERT INTO iot.session (token, user_id) VALUES ('ce089652-fcb9-4946-9177-1412f0096539', 1);
INSERT INTO iot.session (token, user_id) VALUES ('32f8f6f8-ae25-4d34-9b82-08904f4abd73', 1);
INSERT INTO iot.session (token, user_id) VALUES ('55a24cf4-4a60-4e99-b9d6-d5efa1f92369', 1);
INSERT INTO iot.session (token, user_id) VALUES ('cb5b51b0-dd66-41c6-ba40-21a73799e17b', 1);
INSERT INTO iot.session (token, user_id) VALUES ('55252d78-f203-497c-b9d9-89e44c941fab', 1);
INSERT INTO iot.session (token, user_id) VALUES ('14ad5d1b-1796-4d05-9f9a-8859e1bc62d0', 1);
INSERT INTO iot.session (token, user_id) VALUES ('48e764e3-6fee-40bd-afc8-791772125146', 1);
INSERT INTO iot.session (token, user_id) VALUES ('da7da46a-d59a-450d-9b81-083664d84da8', 1);
INSERT INTO iot.session (token, user_id) VALUES ('7f7e78d4-b55d-41da-b098-a6520a64b921', 1);
INSERT INTO iot.session (token, user_id) VALUES ('06c05ee6-475d-4428-8cc5-392c45628da3', 1);
INSERT INTO iot.session (token, user_id) VALUES ('8184e863-ff8f-4fc8-a1e3-9f321874e254', 1);
INSERT INTO iot.session (token, user_id) VALUES ('7c588bf4-67a1-4e89-9bbb-a3ba5dd7ef05', 1);
INSERT INTO iot.session (token, user_id) VALUES ('9282bc8d-4571-4c14-9efb-22965918c84e', 1);
INSERT INTO iot.session (token, user_id) VALUES ('8d240ee0-2db8-42d1-9e16-c0878432faf2', 1);
INSERT INTO iot.session (token, user_id) VALUES ('cd6f92fc-d89d-4304-9fd3-43a9e3967be3', 1);
INSERT INTO iot.session (token, user_id) VALUES ('5e5660cb-8efc-49df-86cf-1430a8ead22c', 1);
INSERT INTO iot.session (token, user_id) VALUES ('9b9fbcfc-2799-48d3-8e0e-ef0de1f6bd1b', 1);
INSERT INTO iot.session (token, user_id) VALUES ('2a8ccf57-63de-45d5-be99-492d91420035', 1);
INSERT INTO iot.session (token, user_id) VALUES ('f03e4809-7a19-4f18-a53e-cbad9014fd1a', 1);
INSERT INTO iot.session (token, user_id) VALUES ('9728c7f2-55aa-492a-82f3-c11e61c67516', 1);
INSERT INTO iot.session (token, user_id) VALUES ('e0361f62-ae21-4e16-9e49-9d5549cc61ba', 1);
INSERT INTO iot.session (token, user_id) VALUES ('d10dbb4e-59bf-4663-b290-05ef936da4bb', 1);
INSERT INTO iot.session (token, user_id) VALUES ('0683746e-8f35-40c1-9eac-ed770709bd92', 1);
INSERT INTO iot.session (token, user_id) VALUES ('320c500b-1dae-4137-aeb0-476b1ffca63e', 1);
INSERT INTO iot.session (token, user_id) VALUES ('44307ea7-5b5b-4ac6-a96d-6f93e2784197', 1);
INSERT INTO iot.session (token, user_id) VALUES ('73369415-893e-4507-b0d2-b7383bc52fea', 1);
INSERT INTO iot.session (token, user_id) VALUES ('68725d62-85af-4be3-a782-fcffd123c341', 1);
INSERT INTO iot.session (token, user_id) VALUES ('1488e85d-7b44-4179-bf14-e19d2df01c65', 1);
INSERT INTO iot.session (token, user_id) VALUES ('294ecd37-879c-4a28-b293-6e203f0e7bbf', 1);
INSERT INTO iot.session (token, user_id) VALUES ('2a3d8863-c911-498c-bc77-6cd78dfc9d13', 1);
INSERT INTO iot.session (token, user_id) VALUES ('1bb100bc-570f-4175-9cff-8583a53b63c5', 1);
INSERT INTO iot.session (token, user_id) VALUES ('3e746969-6cc0-444d-997a-b69c8e059737', 1);
INSERT INTO iot.session (token, user_id) VALUES ('cffa9311-2c2c-46bb-8fc0-6f0b3732e248', 1);
INSERT INTO iot.session (token, user_id) VALUES ('b3014d88-dbb3-4494-82b7-e0616d5c4cde', 1);

INSERT INTO iot.things (description, ip, name, parentUnit_id) VALUES ('32323', null, 'Thing 1', 1);

INSERT INTO iot.rights (role, unit_id, user_id) VALUES ('ALL', 1, 1);

