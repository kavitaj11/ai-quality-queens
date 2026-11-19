describe('Shipping calculator', () => {
  for (const [subtotal, message] of [[4999, 'Shipping: 499 cents'], [5000, 'Free shipping']]) {
    it('quotes a subtotal of ' + subtotal, () => {
      cy.visit('/');
      cy.get('#subtotal').type(String(subtotal));
      cy.contains('button', 'Calculate shipping').click();
      cy.get('[role=status]').should('have.text', message);
    });
  }
});
