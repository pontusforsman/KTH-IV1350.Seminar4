# Requirements Specification for Sale Process

The scenarios below describe basic flow and alternative flows for processing a sale at a point-of-sale (POS) in a retail
store.

---

## Basic Flow

1. Customer arrives at POS with goods to purchase.
2. Cashier starts a new sale.
3. Cashier enters item identifier.
4. Program retrieves price, VAT (tax) rate, and item description from the external inventory system. Program records the
   sold item. Program also presents item description, price, and running total (including VAT).
5. Steps three and four are repeated until the cashier has registered all items.
6. Cashier asks customer if they want to buy anything more.
7. Customer answers s’no’ (a ’yes’ answer is not considered in this scenario).
8. Cashier ends the sale.
9. Program presents total price, including VAT.
10. Cashier tells customer the total, and asks for payment.
11. Customer pays cash.
12. Cashier enters amount paid.
13. Program sends sale information to external accounting system (for accounting) and external inventory system (to
    update inventory).
14. Program increases the amount present in the cashRegister with the amount paid.
15. Program prints receipt and tells how much change to give customer.
16. Customer leaves with receipt and goods.

---

## Alternative Flows

### 3-4a. No item with the specified identifier is found.

1. Program tells that identifier is invalid.

### 3-4b. An item with the specified identifier has already been entered in the current sale.

1. Program increases the sold quantity of the item, and presents item description, price, and running total.

### 3-4c. Customer purchases multiple items of the same goods (with the same identifier), and cashier registers them together.

1. Cashier enters item identifier.
2. Cashier enters quantity.
3. Program calculates price, records the sold item and quantity, and presents item description, price, and running
   total.

### 9a (may also be 10a or 11a) Customer says they are eligible for a discount.

1. Cashier signals discount request.
2. Cashier enters customer identification.
3. Program fetches discount from the discount database, see Business Rules and Clarifications below.
4. Program presents price after discount, based on discount rules. See Business Rules and Clarifications below for more
   details on discounts.

---

## Business Rules and Clarifications

### Taxes/VAT

The VAT mentioned in basic flow, bullets four and nine, is not included in the price stored in the external inventory
system. It must instead be added before the total price is calculated. There are three different VAT rates: 25%, 12% and
6%. Each item description in the item registry must contain information about that item’s VAT rate.

### Receipt

The receipt mentioned in basic flow, bullets 15 and 16, contains the following information:

- Date and time of sale.
- Name, quantity and price for each item.
- Total price for the entire sale.
- VAT for the entire sale.
- Amount paid and change.

### Discounts

The discounts mentioned in alternative flow 9a are calculated based on bought items, total cost for the entire sale, and
customer id. A customer might be eligible for more than one type of discount. There’s already a database which contains
information about all existing discounts, discount information must be fetched from this database. The discount database
contains the following information.

- When passed a list of all bought items, it tells a sum to be reduced from the total cost of the entire sale. The sum
  is zero if there’s no discount.
- When passed the total cost of the entire sale, it tells a percentage to be reduced from this total cost. The
  percentage is zero if there’s no discount.
- When passed the customer id, it tells a percentage to be reduced from the total cost of the entire sale. The
  percentage is zero if there’s no discount.

---

Architectural requirements: 
Model-View-Controller
Layered Architecture
