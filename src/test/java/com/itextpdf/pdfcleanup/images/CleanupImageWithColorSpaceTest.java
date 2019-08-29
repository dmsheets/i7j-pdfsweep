/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.pdfcleanup.images;


import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Category(IntegrationTest.class)
public class CleanupImageWithColorSpaceTest extends ExtendedITextTest {

    private static final String inputPath = "./src/test/resources/com/itextpdf/pdfcleanup/images/CleanupImageWithColorSpaceTest/";
    private static final String outputPath = "./target/test/com/itextpdf/pdfcleanup/images/CleanupImageWithColorSpaceTest/";

    @Rule
    public ExpectedException junitExpectedException = ExpectedException.none();

    @BeforeClass
    public static void before() {
        createOrClearDestinationFolder(outputPath);
    }

@Ignore ("test works with differs in Java and .Net, cause the one doesn't work with 8-byte images")
    @Test
    // TODO: update cmp file after DEVSIX-1908 fixed
    public void cleanUpTestColorSpace() throws IOException, InterruptedException {
        String input = inputPath + "imgSeparationCs.pdf";
        String output = outputPath + "imgSeparationCs.pdf";
        String cmp = inputPath + "cmp_imgSeparationCs.pdf";

        cleanUp(input, output, Arrays.asList(new PdfCleanUpLocation(1, new Rectangle(60f, 780f, 60f, 45f), ColorConstants.GREEN)));
        compareByContent(cmp, output, outputPath, "diff_imgSeparationCs");
    }


    private void cleanUp(String input, String output, List<PdfCleanUpLocation> cleanUpLocations) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(input), new PdfWriter(output));

        PdfCleanUpTool cleaner = (cleanUpLocations == null)
                ? new PdfCleanUpTool(pdfDocument, true)
                : new PdfCleanUpTool(pdfDocument, cleanUpLocations);
        cleaner.cleanUp();

        pdfDocument.close();
    }

    private void compareByContent(String cmp, String output, String targetDir, String diffPrefix) throws IOException, InterruptedException {
        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(output, cmp, targetDir, diffPrefix + "_");

        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}