package io.github.btkelly.gandalf;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings({
        "RV_RETURN_VALUE_IGNORED",
        "magicnumber",
        "TooManyStaticImports"
})
public class GandalfTest {

    public static final String VERSION_NAME = "1.2.3";
    public static final String ALERT_MESSAGE = "Alert Message";
    public static final String LATCH_TIMEOUT_MESSAGE = "Latch timeout";
    private static final String MOCK_PACKAGE = "mock.package";
    private MockWebServer mockWebServer;

    @Mock
    private Context mockContext;
    @Mock
    private PackageManager mockPackageManager;
    @Mock
    private SharedPreferences mockSharedPreferences;

    @Before
    public void init() throws IOException, PackageManager.NameNotFoundException {
        initMocks(this);

        when(mockContext.getPackageName()).thenReturn(MOCK_PACKAGE);
        when(mockContext.getPackageManager()).thenReturn(mockPackageManager);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);

        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
    }

    @After
    public void cleanUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        this.mockWebServer.shutdown();
        this.mockWebServer = null;

        Field gandalfInstance = Gandalf.class.getDeclaredField("gandalfInstance");
        gandalfInstance.setAccessible(true);
        gandalfInstance.set(null, null);
    }

    @Test
    public void invalidVersionCodeSafelyCallsNoActionRequired() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        enqueueMockResponse("invalidVersion.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onNoActionRequired();
    }

    @Test
    public void updateRequiredButHigherVersion() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        enqueueMockResponse("updateRequired.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 8).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onNoActionRequired();
    }

    @Test
    public void updateRequiredAndLowerVersion() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        enqueueMockResponse("updateRequired.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onRequiredUpdate(any(RequiredUpdate.class));
    }

    @Test
    public void updateOptionalButHigherVersion() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        when(mockSharedPreferences.getString(anyString(), anyString())).thenReturn("");

        enqueueMockResponse("updateOptional.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 8).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onNoActionRequired();
    }

    @Test
    public void updateOptionalAndLowerVersion() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        when(mockSharedPreferences.getString(anyString(), anyString())).thenReturn("");

        enqueueMockResponse("updateOptional.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onOptionalUpdate(any(OptionalUpdate.class));
    }

    @Test
    public void updateOptionalAndLowerVersionAlreadySeen() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        when(mockSharedPreferences.getString(anyString(), anyString())).thenReturn("7");

        enqueueMockResponse("updateOptional.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onNoActionRequired();
    }

    @Test
    public void blockingAlert() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        when(mockSharedPreferences.getString(anyString(), anyString())).thenReturn("");

        enqueueMockResponse("blockingAlert.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onAlert(argThat(new ArgumentMatcher<Alert>() {
            @Override
            public boolean matches(Alert alert) {
                return alert.isBlocking() && ALERT_MESSAGE.equals(alert.getMessage());
            }
        }));
    }

    @Test
    public void blockingAlertAlreadySeen() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        when(mockSharedPreferences.getString(anyString(), anyString())).thenReturn(ALERT_MESSAGE);

        enqueueMockResponse("blockingAlert.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onAlert(argThat(new ArgumentMatcher<Alert>() {
            @Override
            public boolean matches(Alert alert) {
                return alert.isBlocking() && ALERT_MESSAGE.equals(alert.getMessage());
            }
        }));
    }

    @Test
    public void nonBlockingAlert() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        when(mockSharedPreferences.getString(anyString(), anyString())).thenReturn("");

        enqueueMockResponse("nonBlockingAlert.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onAlert(argThat(new ArgumentMatcher<Alert>() {
            @Override
            public boolean matches(Alert alert) {
                return !alert.isBlocking() && ALERT_MESSAGE.equals(alert.getMessage());
            }
        }));
    }

    @Test
    public void nonBlockingAlertAlreadySeen() throws IOException, PackageManager.NameNotFoundException, InterruptedException {

        when(mockSharedPreferences.getString(anyString(), anyString())).thenReturn(ALERT_MESSAGE);

        enqueueMockResponse("nonBlockingAlert.json");

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GandalfCallback mockCallback = getGandalfCallback(countDownLatch);
        getGandalfInstance(VERSION_NAME, 6).shallIPass(mockContext, mockCallback);

        boolean status = countDownLatch.await(5, TimeUnit.SECONDS);
        assertTrue(LATCH_TIMEOUT_MESSAGE, status);

        verify(mockCallback).onNoActionRequired();
    }

    private Gandalf getGandalfInstance(String versionName, int versionCode) throws PackageManager.NameNotFoundException {

        PackageInfo packageInfo = new PackageInfo();
        packageInfo.versionName = versionName;
        packageInfo.versionCode = versionCode;

        when(mockPackageManager.getPackageInfo(anyString(), anyInt())).thenReturn(packageInfo);

        HttpUrl bootStrapUrl = mockWebServer.url("/bootStrap");

        ExecutorService directExecutorService = Executors.newSingleThreadExecutor();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(directExecutorService))
                .build();

        new Gandalf.Installer()
                .setContext(mockContext)
                .setPackageName(MOCK_PACKAGE)
                .setBootstrapUrl(bootStrapUrl.toString())
                .setOkHttpClient(okHttpClient)
                .setCallbackExecutor(directExecutorService)
                .install();

        return Gandalf.get();
    }

    private void enqueueMockResponse(String bodyFileName) throws IOException {

        String jsonBody = IOUtils.resourceToString(
                bodyFileName,
                Charset.defaultCharset(),
                getClass().getClassLoader()
        );

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(jsonBody);

        this.mockWebServer.enqueue(mockResponse);
    }

    private GandalfCallback getGandalfCallback(final CountDownLatch countDownLatch) {
        return spy(new GandalfCallback() {
            @Override
            public void onRequiredUpdate(RequiredUpdate requiredUpdate) {
                countDownLatch.countDown();
            }

            @Override
            public void onOptionalUpdate(OptionalUpdate optionalUpdate) {
                countDownLatch.countDown();
            }

            @Override
            public void onAlert(Alert alert) {
                countDownLatch.countDown();
            }

            @Override
            public void onNoActionRequired() {
                countDownLatch.countDown();
            }
        });
    }

}
