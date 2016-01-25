/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.github.btkelly.gandalf.checker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.github.btkelly.gandalf.models.Alert;
import io.github.btkelly.gandalf.models.AppVersionDetails;
import io.github.btkelly.gandalf.models.Bootstrap;
import io.github.btkelly.gandalf.models.OptionalUpdate;
import io.github.btkelly.gandalf.models.RequiredUpdate;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class GateKeeperTest {

    private static final String MOCK_PACKAGE = "mock.package";
    private static final String ASSERT_TRUE_MESSAGE = "Should be true";
    private static final String ASSERT_FALSE_MESSAGE = "Should be false";

    private GateKeeper subject;


    @Mock private Context mockContext;
    @Mock private PackageManager mockPackageManager;
    @Mock private PackageInfo mockPackageInfo;

    @Mock private Bootstrap mockBootstrap;
    @Mock private VersionChecker mockVersionChecker;
    @Mock private HistoryChecker mockHistoryChecker;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mockContext.getPackageName()).thenReturn(MOCK_PACKAGE);
        when(mockContext.getPackageManager()).thenReturn(mockPackageManager);
        when(mockPackageManager.getPackageInfo(anyString(), anyInt())).thenReturn(mockPackageInfo);

        when(mockBootstrap.getRequiredUpdate()).thenReturn(Mockito.mock(RequiredUpdate.class));
        when(mockBootstrap.getOptionalUpdate()).thenReturn(Mockito.mock(OptionalUpdate.class));
        when(mockBootstrap.getAlert()).thenReturn(Mockito.mock(Alert.class));

        subject = new GateKeeper(mockContext, mockVersionChecker, mockHistoryChecker);

    }

    //RequiredUpdate tests
    @Test
    public void showRequiredUpdateShouldReturnFalseWhenRequiredUpdateIsNull() {
        when(mockBootstrap.getRequiredUpdate()).thenReturn(null);

        Assert.assertFalse("RequiredUpdate is null, this should be false", subject.updateIsRequired(mockBootstrap));
    }

    @Test
    public void showRequiredUpdateShouldBeTrueWhenVersionCheckRequiresUpdate() {
        when(mockVersionChecker.showRequiredUpdate(Mockito.any(RequiredUpdate.class), Mockito.any(AppVersionDetails.class))).thenReturn(true);

        Assert.assertTrue(ASSERT_TRUE_MESSAGE, subject.updateIsRequired(mockBootstrap));
    }

    @Test
    public void showRequiredUpdateShouldBeFalseWhenVersionCheckFalse() {
        when(mockVersionChecker.showRequiredUpdate(Mockito.any(RequiredUpdate.class), Mockito.any(AppVersionDetails.class))).thenReturn(false);

        Assert.assertFalse(ASSERT_FALSE_MESSAGE, subject.updateIsRequired(mockBootstrap));
    }

    //OptionalUpdate tests
    @Test
    public void showOptionalUpdateShouldBeFalseIfOptionalUpdateIsNull() {
        when(mockBootstrap.getOptionalUpdate()).thenReturn(null);

        Assert.assertFalse("OptionalUpdate is null, should be false", subject.updateIsOptional(mockBootstrap));
    }

    @Test
    public void showOptionalUpdateShouldBeTrueWithAvailableTrueAndHistoryFalse() {
        setOptionalUpdateScenario(true, false);

        Assert.assertTrue(ASSERT_TRUE_MESSAGE, subject.updateIsOptional(mockBootstrap));
    }

    @Test
    public void showOptionalUpdateShouldBeFalseWithAvailableTrueAndHistoryTrue() {
        setOptionalUpdateScenario(true, true);

        Assert.assertFalse(ASSERT_FALSE_MESSAGE, subject.updateIsOptional(mockBootstrap));
    }

    @Test
    public void showOptionalUpdateShouldBeFalseWithAvailableFalseAndHistoryFalse() {
        setOptionalUpdateScenario(false, false);

        Assert.assertFalse(ASSERT_FALSE_MESSAGE, subject.updateIsOptional(mockBootstrap));
    }

    @Test
    public void showOptionalUpdateShouldBeFalseWithAvailableFalseAndHistoryTrue() {
        setOptionalUpdateScenario(false, true);

        Assert.assertFalse(ASSERT_FALSE_MESSAGE, subject.updateIsOptional(mockBootstrap));
    }


    //Alert tests
    @Test
    public void showAlertShouldBeFalseWhenAlertIsNull() {
        when(mockBootstrap.getAlert()).thenReturn(null);

        Assert.assertFalse("Alert is null, should be false", subject.showAlert(mockBootstrap));
    }

    @Test
    public void showAlertShouldBeTrueIfShowIsTrue() {
        setAlertScenario(true, false);

        Assert.assertTrue(ASSERT_TRUE_MESSAGE, subject.showAlert(mockBootstrap));
    }

    @Test
    public void showAlertShouldBeTrueIfShowIsTrueAndHistoryMatches() {
        setAlertScenario(true, true);

        Assert.assertTrue(ASSERT_TRUE_MESSAGE, subject.showAlert(mockBootstrap));
    }

    @Test
    public void showAlertShouldBeTrueIfNotContainedInHistory() {
        setAlertScenario(false, false);

        Assert.assertTrue(ASSERT_TRUE_MESSAGE, subject.showAlert(mockBootstrap));
    }

    @Test
    public void showAlertShouldBeFalseIfHistoryMatchesAndShowIsFalse() {
        setAlertScenario(false, true);

        Assert.assertFalse(ASSERT_FALSE_MESSAGE, subject.showAlert(mockBootstrap));
    }

    private void setAlertScenario(final boolean alertShouldBeShown, final boolean alertMatchesHistory) {
        when(mockVersionChecker.showAlert(Mockito.any(Alert.class))).thenReturn(alertShouldBeShown);
        when(mockHistoryChecker.contains(Mockito.any(Alert.class))).thenReturn(alertMatchesHistory);
    }

    private void setOptionalUpdateScenario(final boolean updateAvailable, final boolean updateMatchesHistory) {
        when(mockVersionChecker.showOptionalUpdate(Mockito.any(OptionalUpdate.class),
                Mockito.any(AppVersionDetails.class))).thenReturn(updateAvailable);
        when(mockHistoryChecker.contains(Mockito.any(OptionalUpdate.class))).thenReturn(updateMatchesHistory);
    }

}
