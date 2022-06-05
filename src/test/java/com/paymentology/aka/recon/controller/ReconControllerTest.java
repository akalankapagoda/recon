package com.paymentology.aka.recon.controller;

import com.paymentology.aka.recon.exception.ReconException;
import com.paymentology.aka.recon.model.*;
import com.paymentology.aka.recon.services.StorageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReconControllerTest {

    @Autowired
    private ReconController reconController;


    @Autowired
    private StorageService storageService;

    private static final String BASE_FILE_NAME = "baseFile";
    private static final String FILE_WITH_ADDITIONAL_RECORD_NAME = "fileWithAdditionalRecord";
    private static final String FILE_WITH_MATCHING_WALLET_REF_NAME = "fileWithMatchingWalletRef";
    private static final String FILE_WITH_MATCHING_OPTIONAL_REF_NAME = "fileWithMatchingOptionalRef";

    private static final String BASE_TX_ID = "baseTransactionId";
    private static final String ADDITIONAL_TX_ID = "additionalTransactionId";
    private static final String WALLET_REF_TX_ID = "walletRefTransactionId";
    private static final String OPTIONAL_REF_TX_ID = "optionalRefTransactionId";


    /**
     * A sample transaction.
     */
    private static Transaction baseTransaction;

    /**
     * A transaction that is different th at the above.
     */
    private static Transaction additionalTransaction;

    /**
     * A transaction where wallet reference, amount and description are similar to the base Tx.
     */
    private static Transaction walletRefTransaction;

    /**
     * A transaction where Transaction Id, Description, Narrative and Amount are similar to the base Tx.
     */
    private static Transaction optionalRefTransaction;

    private static ProcessingResults baseResults;
    private static ProcessingResults additionalResults;
    private static ProcessingResults walletRefResults;
    private static ProcessingResults optionalRefResults;


    @BeforeAll
    private static void init() {

        baseTransaction = new Transaction();
        baseTransaction.setTransactionId(BASE_TX_ID);
        baseTransaction.setDescription("baseTxDescription");
        baseTransaction.setNarrative("baseNarrative");
        baseTransaction.setAmount(new BigInteger("1000"));
        baseTransaction.setWalletReference("baseTxWalletRef");

        additionalTransaction = new Transaction();
        additionalTransaction.setTransactionId(ADDITIONAL_TX_ID);
        additionalTransaction.setDescription("additionalTxDescription");
        additionalTransaction.setNarrative("additionalNarrative");
        additionalTransaction.setAmount(new BigInteger("2000"));
        additionalTransaction.setWalletReference("additionalTxWalletRef");

        walletRefTransaction = new Transaction();
        walletRefTransaction.setTransactionId(WALLET_REF_TX_ID);
        walletRefTransaction.setDescription("baseTxDescription");
        walletRefTransaction.setNarrative("walletRefNarrative");
        walletRefTransaction.setAmount(new BigInteger("1000"));
        walletRefTransaction.setWalletReference("baseTxWalletRef");

        optionalRefTransaction = new Transaction();
        optionalRefTransaction.setTransactionId(OPTIONAL_REF_TX_ID);
        optionalRefTransaction.setDescription("baseTxDescription");
        optionalRefTransaction.setNarrative("baseNarrative");
        optionalRefTransaction.setAmount(new BigInteger("1000"));
        optionalRefTransaction.setWalletReference("optionalRefTxWalletRef");

        baseResults = new ProcessingResults(BASE_FILE_NAME);
        baseResults.setStatus(ReconStatus.SUCCESS);
        baseResults.addTransaction(baseTransaction);

        additionalResults = new ProcessingResults(FILE_WITH_ADDITIONAL_RECORD_NAME);
        additionalResults.setStatus(ReconStatus.SUCCESS);
        additionalResults.addTransaction(baseTransaction);
        additionalResults.addTransaction(additionalTransaction);

        walletRefResults = new ProcessingResults(FILE_WITH_MATCHING_WALLET_REF_NAME);
        walletRefResults.setStatus(ReconStatus.SUCCESS);
        walletRefResults.addTransaction(walletRefTransaction);

        optionalRefResults = new ProcessingResults(FILE_WITH_MATCHING_OPTIONAL_REF_NAME);
        optionalRefResults.setStatus(ReconStatus.SUCCESS);
        optionalRefResults.addTransaction(optionalRefTransaction);

    }

    /**
     * Test same file as source and target.
     *
     * @throws ReconException
     */
    @Test
    public void testReconWithSameFile() throws ReconException {

        storageService.saveProcessingResults(BASE_FILE_NAME, baseResults);

        Response response = reconController.reconcileTransactions(BASE_FILE_NAME, BASE_FILE_NAME);

        assertThat(response.getStatus()).isEqualTo(ReconStatus.SUCCESS);

        ReconciliationResults results = waitForProcessingAndGetResults(BASE_FILE_NAME, BASE_FILE_NAME);

        assertThat(results).isNotNull();

        assertThat(results.getMatchCount()).isEqualTo(1);
        assertThat(results.getSourceRecordsCount()).isEqualTo(1);
        assertThat(results.getTargetRecordsCount()).isEqualTo(1);
        assertThat(results.getSourceSuggestions().isEmpty()).isTrue();
        assertThat(results.getTargetSuggestions().isEmpty()).isTrue();
        assertThat(results.getUnmatchedSourceTransactions().isEmpty()).isTrue();
        assertThat(results.getUnmatchedTargetTransactions().isEmpty()).isTrue();

    }

    /**
     * The target will have one unmatched record.
     *
     * @throws ReconException
     */
    @Test
    public void testReconWithAnAdditionalRecordOnTargetFile() throws ReconException {

        storageService.saveProcessingResults(BASE_FILE_NAME, baseResults);
        storageService.saveProcessingResults(FILE_WITH_ADDITIONAL_RECORD_NAME, additionalResults);

        Response response = reconController.reconcileTransactions(BASE_FILE_NAME, FILE_WITH_ADDITIONAL_RECORD_NAME);

        assertThat(response.getStatus()).isEqualTo(ReconStatus.SUCCESS);

        ReconciliationResults results = waitForProcessingAndGetResults(BASE_FILE_NAME, FILE_WITH_ADDITIONAL_RECORD_NAME);

        assertThat(results).isNotNull();

        assertThat(results.getMatchCount()).isEqualTo(1);
        assertThat(results.getSourceRecordsCount()).isEqualTo(1);
        assertThat(results.getTargetRecordsCount()).isEqualTo(2);
        assertThat(results.getSourceSuggestions().isEmpty()).isTrue();
        assertThat(results.getTargetSuggestions().isEmpty()).isTrue();
        assertThat(results.getUnmatchedSourceTransactions().isEmpty()).isTrue();
        assertThat(results.getUnmatchedTargetTransactions().size()).isEqualTo(1);
        assertThat(results.getUnmatchedTargetTransactions().get(0).getTransactionId()).isEqualTo(ADDITIONAL_TX_ID);

    }

    /**
     * The target will have a matching wallet reference.
     *
     * @throws ReconException
     */
    @Test
    public void testReconWithWalletRef() throws ReconException {

        storageService.saveProcessingResults(BASE_FILE_NAME, baseResults);
        storageService.saveProcessingResults(FILE_WITH_MATCHING_WALLET_REF_NAME, walletRefResults);

        Response response = reconController.reconcileTransactions(BASE_FILE_NAME, FILE_WITH_MATCHING_WALLET_REF_NAME);

        assertThat(response.getStatus()).isEqualTo(ReconStatus.SUCCESS);

        ReconciliationResults results = waitForProcessingAndGetResults(BASE_FILE_NAME, FILE_WITH_MATCHING_WALLET_REF_NAME);

        assertThat(results).isNotNull();

        assertThat(results.getMatchCount()).isEqualTo(0);
        assertThat(results.getSourceRecordsCount()).isEqualTo(1);
        assertThat(results.getTargetRecordsCount()).isEqualTo(1);
        assertThat(results.getSourceSuggestions().size()).isEqualTo(1);
        assertThat(results.getSourceSuggestions().get(0).getTransactionId()).isEqualTo(BASE_TX_ID);
        assertThat(results.getTargetSuggestions().size()).isEqualTo(1);
        assertThat(results.getTargetSuggestions().get(0).getTransactionId()).isEqualTo(WALLET_REF_TX_ID);
        assertThat(results.getUnmatchedSourceTransactions().isEmpty()).isTrue();
        assertThat(results.getUnmatchedTargetTransactions().isEmpty()).isTrue();

    }

    /**
     * Test target transaction with optional reference.
     *
     * @throws ReconException
     */
    @Test
    public void testReconWithOptionalRef() throws ReconException {

        storageService.saveProcessingResults(BASE_FILE_NAME, baseResults);
        storageService.saveProcessingResults(FILE_WITH_MATCHING_OPTIONAL_REF_NAME, optionalRefResults);

        Response response = reconController.reconcileTransactions(BASE_FILE_NAME, FILE_WITH_MATCHING_OPTIONAL_REF_NAME);

        assertThat(response.getStatus()).isEqualTo(ReconStatus.SUCCESS);

        ReconciliationResults results = waitForProcessingAndGetResults(BASE_FILE_NAME, FILE_WITH_MATCHING_OPTIONAL_REF_NAME);

        assertThat(results).isNotNull();

        assertThat(results.getMatchCount()).isEqualTo(0);
        assertThat(results.getSourceRecordsCount()).isEqualTo(1);
        assertThat(results.getTargetRecordsCount()).isEqualTo(1);
        assertThat(results.getSourceSuggestions().size()).isEqualTo(1);
        assertThat(results.getSourceSuggestions().get(0).getTransactionId()).isEqualTo(BASE_TX_ID);
        assertThat(results.getTargetSuggestions().size()).isEqualTo(1);
        assertThat(results.getTargetSuggestions().get(0).getTransactionId()).isEqualTo(OPTIONAL_REF_TX_ID);
        assertThat(results.getUnmatchedSourceTransactions().isEmpty()).isTrue();
        assertThat(results.getUnmatchedTargetTransactions().isEmpty()).isTrue();

    }

    /**
     * Poll until processing is complete.
     *
     * Poll each second for 5 seconds only. We expect this to be processed pretty fast since we have only 1 or 2 Txs.
     *
     * @param source
     * @param target
     *
     * @return Recon results or null if not found
     * @throws ReconException
     */
    private ReconciliationResults waitForProcessingAndGetResults(String source, String target) throws ReconException {

        long timeout = 5000;

        while (timeout > 0) {
            Response response = reconController.getReconResults(source, target);

            if (response.getStatus() == ReconStatus.SUCCESS) {

                assertThat(response instanceof ReconciliationResults).isTrue();

                return (ReconciliationResults) response;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {
                    // Ignore. There's nothing we can do about this
                }

                timeout--;
            }
        }

        return null;
    }
}
