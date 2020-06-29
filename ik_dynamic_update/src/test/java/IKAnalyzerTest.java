import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @Author: seastliu
 * @Description:
 * @Date: Create in 17:16 2019/12/26
 * @Modified by:
 **/
public class IKAnalyzerTest {

    @Test
    public void testAnalyzer() throws Exception {
        IKAnalyzer analyzer = new IKAnalyzer();
        TokenStream ts = analyzer.tokenStream("text", "中航善达");
        CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
        ts.reset();

        while (ts.incrementToken()) {
            System.out.println(term.toString());
        }

        ts.end();
        ts.close();
    }
}
