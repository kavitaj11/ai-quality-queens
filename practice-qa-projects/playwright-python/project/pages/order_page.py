from playwright.sync_api import Page


class OrderPage:
    def __init__(self, page: Page):
        self.page = page
        self.order_id = page.get_by_test_id("order-id")
        self.status = page.get_by_test_id("order-status")
        self.total = page.get_by_test_id("order-total")
