import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ChefRequest} from "../../shared/models/chef-request";
import {MediaService} from "../../shared/services/media.service";
import {UserService} from "../../shared/services/user.service";
import {Router} from "@angular/router";
import {ToasterService} from "angular2-toaster";
import {Media} from "../../shared/models/media";

@Component({
  selector: 'app-upgrade-chef',
  templateUrl: './upgrade-chef.component.html',
  styleUrls: ['./upgrade-chef.component.css']
})
export class UpgradeChefComponent implements OnInit {

  private textChefForm: FormGroup;
  private chefRequest: ChefRequest = new ChefRequest();
  private image: Media;
  private uploadingImage: boolean;

  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(private fb: FormBuilder,
              private mediaService: MediaService,
              private userService: UserService,
              private toaster: ToasterService,
              private router: Router){
    this.textChefForm = fb.group({
      'resumeChef': new FormControl(null, [Validators.required]),
    });
  }

  ngOnInit(): void {}

  postChefRequest() {
    this.chefRequest.resume = this.textChefForm.value.resumeChef;
    this.chefRequest.media = this.image;
    console.log(this.chefRequest);
    this.userService.postChefRequest(this.chefRequest).then(() => {
        this.toaster.pop("success", "Solicitud enviada correctamente");
        this.router.navigate(['']);
      }, () => { this.toaster.pop("error", "No se pudo enviar"); }
    );
  }

  public addImage(e: Event) {
    e.preventDefault();
    this.uploadingImage = true;
    const files = (<HTMLInputElement> document.getElementById('image')).files;
    if (!files.length) return;
    this.mediaService.uploadMedia(files[0]).then(media => {
      this.uploadingImage = false;
      (<HTMLInputElement> document.getElementById('image')).value = '';
      this.image = media;
    }, () => { (<HTMLInputElement> document.getElementById('image')).value = ''; });
  }

  public get imageButtonText(): string {
    if (this.uploadingImage) return 'Subiendo';
    else return 'Agregar';
  }

  public removeImage() {
    this.chefRequest.media = null;
    this.image = null;
  }

  private disableButton(): boolean {
    return (!this.textChefForm.valid && this.image != null);
  }

}


