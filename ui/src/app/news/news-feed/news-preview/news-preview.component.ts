import {Component, Input, OnInit} from '@angular/core';
import {News} from "../../../shared/models/news";
import {User} from "../../../shared/models/user-model";
import {FormatService} from "../../../shared/services/format.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-news-preview',
  templateUrl: './news-preview.component.html',
  styleUrls: ['./news-preview.component.css']
})
export class NewsPreviewComponent implements OnInit {

  @Input() news: News;
  private embedVideoUrl;

  constructor(
    private formatter: FormatService,
    private sanitizer: DomSanitizer,
  ) {
  }

  ngOnInit() {
    if (this.news && this.news.videoUrl) this.embedVideoUrl = this.setEmbedVideoUrl(this.news.videoUrl);
  }

  private get description(): string {
    const max = 255;
    if (this.news.description.length > max)
      return this.news.description.substr(0, max) + '...';
    else return this.news.description;
  }

  private get thumbnailUrl(): string {
    if (this.news.image) return this.news.image.url;
    else if (this.news.videoUrl) return this.formatter.youtubeThumbnailUrl(this.news.videoUrl);
  }

  private setEmbedVideoUrl(u) {
    const split = u.split('v=');
    const url = `https://www.youtube.com/embed/${split[1]}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  private mock() {
    this.news = new News;
    this.news.title = 'Les recomiendo esta cuchara';
    this.news.author = new User;
    this.news.author.name = "Jorge";
    this.news.author.lastName = "Lopez";
    this.news.created = new Date;
    this.news.videoUrl = "https://www.youtube.com/watch?v=zMvHXKcvqAs";
    this.news.description = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque at egestas turpis. Praesent at ipsum nisl. Nunc eu nunc bibendum enim gravida cursus. Integer ac libero id tortor facilisis luctus vel at lectus. Sed lacinia scelerisque lacus convallis faucibus. Sed id nibh maximus, finibus purus ac, interdum elit. Sed consectetur interdum tortor, id accumsan ipsum lacinia quis. Sed elementum, risus vel finibus condimentum, elit massa consectetur diam, ut aliquet odio libero quis orci.\n' +
      '\n' +
      'Ut ut ultrices ex, eget varius metus. Duis ornare rutrum mi, ac ullamcorper justo commodo viverra. Sed pretium placerat nulla dictum luctus. Integer bibendum laoreet ante, sit amet dictum lacus interdum eu. Sed nec turpis et ligula feugiat suscipit sed sed ante. Morbi felis sapien, sollicitudin ac eros eu, lacinia pharetra leo. Nam venenatis dolor facilisis enim dictum viverra. Nulla neque urna, varius eget efficitur id, euismod at felis. Donec vestibulum velit a odio volutpat sagittis. Vestibulum congue auctor ante, id bibendum risus auctor in.\n' +
      '\n' +
      'Suspendisse et dolor augue. Suspendisse accumsan lectus vel gravida cursus. Curabitur suscipit metus molestie lacinia sollicitudin. Curabitur finibus elementum varius. Morbi blandit sodales libero, ac porttitor sapien mollis id. Integer consequat nisi ac tempus tincidunt. Cras odio nisl, venenatis condimentum aliquet sed, egestas vitae mi. Proin euismod scelerisque eleifend. Nunc vel ipsum at mauris laoreet varius eget sit amet dolor. Maecenas ac diam felis.\n' +
      '\n' +
      'Morbi fringilla nisi ex, eu lobortis magna lacinia non. Suspendisse iaculis magna eu sagittis laoreet. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Pellentesque pulvinar enim sit amet semper iaculis. Mauris condimentum lorem orci, id rhoncus eros feugiat eget. Nam vehicula lorem tortor, in pellentesque ex tincidunt id. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce ac pulvinar arcu, ut iaculis neque. Ut pharetra odio a nunc mattis, sed iaculis turpis condimentum. Duis dictum porta purus eget dapibus. Nam viverra feugiat lectus.\n' +
      '\n' +
      'Donec eget quam et nulla interdum accumsan at in enim. Nullam dictum est purus, sit amet feugiat mi volutpat et. Morbi hendrerit, ipsum et sagittis porta, justo turpis accumsan magna, sit amet euismod magna ligula in orci. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Cras mattis maximus egestas. Nulla a tincidunt neque. Pellentesque feugiat pharetra tristique. Donec ac risus non tellus pulvinar tempus. Suspendisse sem nunc, varius a mi nec, malesuada pulvinar velit.\n' +
      '\n' +
      'Morbi fringilla porta dolor, a rutrum quam aliquam eu. Proin ut nibh quis arcu pharetra scelerisque et eget ligula. Duis ac dictum nisi. Morbi eget dolor ut eros scelerisque pharetra eu ut augue. Sed at ex eget tellus elementum pharetra. Praesent posuere ac sapien id rutrum. Nullam interdum nisl massa, sed scelerisque lorem dictum eu. Vestibulum rhoncus sapien a elit finibus, sit amet commodo justo iaculis. Nullam lectus purus, placerat id egestas a, fringilla nec metus. Nullam sit amet nisl orci. Donec convallis sagittis fringilla. Morbi consectetur blandit lectus in posuere. Nullam eu tortor vitae nunc porta dignissim. Phasellus nec lectus felis. Pellentesque tincidunt sit amet sapien id lobortis.\n' +
      '\n' +
      'Donec accumsan, odio sit amet porta vestibulum, ante velit dignissim turpis, eu pretium nisi diam id nibh. Fusce faucibus, nulla ut molestie sodales, magna est tempus eros, eu dapibus sapien turpis finibus ex. Mauris interdum turpis libero, sed fringilla ipsum congue bibendum. Pellentesque consequat gravida augue eget luctus. Maecenas tempus urna dolor, non tincidunt nulla tempor non. Nullam sodales leo vehicula mauris malesuada, sed interdum diam consectetur. Morbi vehicula nunc sit amet metus mollis finibus. Sed tincidunt sollicitudin nibh eget tincidunt. Quisque euismod maximus nisi, vitae rutrum arcu bibendum eget. Nullam massa diam, ultrices quis laoreet ut, gravida vel ligula. Pellentesque ultrices tellus et metus elementum pellentesque. Nam a dolor nibh.\n' +
      '\n' +
      'Fusce id pharetra ante. Aenean non metus iaculis elit rhoncus dignissim eu et felis. Vestibulum congue consequat tincidunt. Phasellus sit amet elit vitae sapien consequat tempor. Aliquam ac justo accumsan, commodo sem id, tincidunt ligula. Vivamus et risus ullamcorper, volutpat est sit amet, pharetra ex. Cras nec mi lacinia, hendrerit nibh eget, convallis enim. Cras lacus felis, aliquam sit amet nisl sed, accumsan scelerisque turpis.\n' +
      '\n' +
      'Nulla volutpat ligula eu tortor vestibulum commodo. Mauris ornare nibh ac maximus congue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam augue tortor, mattis ut vulputate non, elementum eu libero. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec posuere nec purus sed consequat. Morbi laoreet, tortor vel fringilla cursus, mi nisl semper dui, sed ullamcorper elit velit et ipsum. Fusce luctus convallis sem sit amet dignissim. Phasellus a lobortis urna. Quisque non nulla a dolor porttitor bibendum eget et elit. Mauris vehicula consectetur felis, quis tempus ante pharetra bibendum.\n' +
      '\n' +
      'In maximus pulvinar tellus, non scelerisque diam pulvinar sed. Duis interdum erat a maximus malesuada. Nunc porttitor nisi nec nisi congue venenatis. Integer mollis nibh purus, et rutrum lorem ultricies vitae. Pellentesque venenatis libero iaculis tincidunt sollicitudin. Donec egestas massa justo, quis ullamcorper est feugiat vulputate. Donec velit magna, finibus pulvinar arcu quis, pretium sodales enim. Maecenas interdum nunc tortor, in posuere massa rhoncus eget. Duis pretium, erat imperdiet tempor congue, mauris massa rutrum nulla, eget pulvinar ligula ex nec neque. Sed sagittis vulputate eros.\n' +
      '\n' +
      'Donec quis lacus leo. Sed consectetur id eros et dignissim. Nulla lectus orci, gravida eu vestibulum quis, consequat nec libero. Mauris in metus ut velit sagittis porta nec id enim. Curabitur sollicitudin pretium orci vitae auctor. Aliquam bibendum mollis ipsum, id tempus lorem pellentesque at. Aliquam quis consectetur velit, nec imperdiet urna.\n' +
      '\n' +
      'Duis non velit vitae ipsum sagittis scelerisque ac sed velit. Donec sapien diam, egestas a metus sit amet, ornare viverra nisi. Etiam turpis nibh, ultricies nec facilisis vitae, consequat ut quam. Cras mollis consectetur enim. Aliquam erat volutpat. Sed maximus finibus elit, ac placerat felis ultricies vel. Donec ut enim tortor. Praesent id euismod orci. Aenean ultricies mattis tortor, quis mattis quam pulvinar accumsan. Donec sed pulvinar nisi. Morbi ornare mollis convallis. Nullam tincidunt et ante eu posuere. Nam a dolor id sapien tempor sodales. Nunc in commodo nibh, ac pellentesque libero. Duis eleifend nunc quis metus consequat viverra vitae ut velit. Mauris posuere consectetur massa, quis viverra urna.';
  }
}
