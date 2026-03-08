public class CustomProductState implements SpecialTransactionState {
    @Override
    public void process(SpecialTransactionContext ctx) {
        ctx.createVM.createSpecialCustomProduct();
        ctx.isFinished = true; // Ends the test feature session after creation
    }
}