import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { ParkingSharedModule } from 'app/shared';
import {
    ParkingPlaceComponent,
    ParkingPlaceDetailComponent,
    ParkingPlaceUpdateComponent,
    ParkingPlaceDeletePopupComponent,
    ParkingPlaceDeleteDialogComponent,
    parkingPlaceRoute,
    parkingPlacePopupRoute
} from './';

const ENTITY_STATES = [...parkingPlaceRoute, ...parkingPlacePopupRoute];

@NgModule({
    imports: [ParkingSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ParkingPlaceComponent,
        ParkingPlaceDetailComponent,
        ParkingPlaceUpdateComponent,
        ParkingPlaceDeleteDialogComponent,
        ParkingPlaceDeletePopupComponent
    ],
    entryComponents: [
        ParkingPlaceComponent,
        ParkingPlaceUpdateComponent,
        ParkingPlaceDeleteDialogComponent,
        ParkingPlaceDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ParkingParkingPlaceModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
