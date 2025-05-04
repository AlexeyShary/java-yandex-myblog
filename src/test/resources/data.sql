-- === Tags ===
INSERT INTO tags (name) VALUES ('tag1');
INSERT INTO tags (name) VALUES ('tag2');
INSERT INTO tags (name) VALUES ('tag3');
INSERT INTO tags (name) VALUES ('tag4');
INSERT INTO tags (name) VALUES ('tag5');

-- === Posts ===
INSERT INTO posts (title, text, image_url, likes_count) VALUES
    ('Post 1 title',
     'Post 1 text',
     NULL,
     1);

INSERT INTO posts (title, text, image_url, likes_count) VALUES
    ('Post 2 title',
     'Post 2 text',
     NULL,
     2);

INSERT INTO posts (title, text, image_url, likes_count) VALUES
    ('Post 3 title',
     'Post 3 text',
     NULL,
     3);

-- === Post-Tag Mapping ===
INSERT INTO post_tags (post_id, tag_id) VALUES (1, 1);
INSERT INTO post_tags (post_id, tag_id) VALUES (1, 2);
INSERT INTO post_tags (post_id, tag_id) VALUES (1, 3);

INSERT INTO post_tags (post_id, tag_id) VALUES (2, 2);

INSERT INTO post_tags (post_id, tag_id) VALUES (3, 5);
INSERT INTO post_tags (post_id, tag_id) VALUES (3, 4);
INSERT INTO post_tags (post_id, tag_id) VALUES (3, 3);

-- === Comments ===
INSERT INTO comments (post_id, text) VALUES (1, 'Comment 1 - 1');
INSERT INTO comments (post_id, text) VALUES (1, 'Comment 1 - 2');
INSERT INTO comments (post_id, text) VALUES (2, 'Comment 2 - 1');