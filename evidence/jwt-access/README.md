# JWT access lab: Postman results

The Postman collection obtains JWTs for `viewer`, `editor`, and `admin`, then runs the 10 numbered requests in order. The Postman Runner completed all 13 requests with **13 passed, 0 failed, 0 errors**. These images are cropped directly from the corresponding Runner result rows. The screenshots used a temporary server on port `8081`.

| # | Request | Expected | Screenshot |
| --- | --- | --- | --- |
| 1 | VIEWER reads list | 200 | [01-viewer-read-list.jpg](01-viewer-read-list.jpg) |
| 2 | VIEWER cannot create | 403 | [02-viewer-create-denied.jpg](02-viewer-create-denied.jpg) |
| 3 | EDITOR reads list | 200 | [03-editor-read-list.jpg](03-editor-read-list.jpg) |
| 4 | EDITOR creates product | 201 | [04-editor-create.jpg](04-editor-create.jpg) |
| 5 | VIEWER reads product | 200 | [05-viewer-read-product.jpg](05-viewer-read-product.jpg) |
| 6 | VIEWER cannot update | 403 | [06-viewer-update-denied.jpg](06-viewer-update-denied.jpg) |
| 7 | EDITOR updates product | 200 | [07-editor-update.jpg](07-editor-update.jpg) |
| 8 | EDITOR cannot delete | 403 | [08-editor-delete-denied.jpg](08-editor-delete-denied.jpg) |
| 9 | ADMIN reads product | 200 | [09-admin-read-product.jpg](09-admin-read-product.jpg) |
| 10 | ADMIN deletes product | 204 | [10-admin-delete.jpg](10-admin-delete.jpg) |

Each screenshot shows the request name, URL, actual response code, and passing Postman test. The product created in request 4 is deleted in request 10.
